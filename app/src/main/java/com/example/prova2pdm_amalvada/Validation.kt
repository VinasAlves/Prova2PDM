package com.example.prova2pdm_amalvada

fun validarCampos(
    nomeCafe: String,
    notaCafe: String,
    aroma: String,
    acidez: String,
    amargor: String,
    sabor: String,
    precoCafe: String
): Boolean {
    return nomeCafe.isNotBlank() &&
            notaCafe.isNotBlank() &&
            aroma.isNotBlank() &&
            acidez.isNotBlank() &&
            amargor.isNotBlank() &&
            sabor.isNotBlank() &&
            precoCafe.isNotBlank()
}