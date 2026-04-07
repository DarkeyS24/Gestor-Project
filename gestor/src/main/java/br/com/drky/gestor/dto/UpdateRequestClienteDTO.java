package br.com.drky.gestor.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateRequestClienteDTO(@NotBlank String telefone, String email) {
}