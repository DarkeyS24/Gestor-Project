package br.com.drky.gestor.dto;

import jakarta.validation.constraints.NotBlank;

public record RequestVerificacaoDTO(@NotBlank String tipo, @NotBlank String documento) {
}