package com.kseniiashinkar.hackathon_platform.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.*

@RestControllerAdvice
class GlobalExceptionHandler {

    // Обработка ошибок валидации
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage ?: "Validation error"
        }

        return ResponseEntity.badRequest().body(mapOf(
            "timestamp" to Date().time,  // Используем Long вместо Date
            "status" to HttpStatus.BAD_REQUEST.value(),
            "errors" to errors
        ))
    }

    // Обработка бизнес-логики ошибок
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.badRequest().body(mapOf(
            "timestamp" to Date().time,  // Используем Long вместо Date
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to (ex.message ?: "Bad request")
        ))
    }

    // Обработка IllegalStateException
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.badRequest().body(mapOf(
            "timestamp" to Date().time,
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to (ex.message ?: "Invalid state")
        ))
    }

    // Обработка остальных исключений
    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf(
            "timestamp" to Date().time,
            "status" to HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "error" to "Internal server error"
        ))
    }
}