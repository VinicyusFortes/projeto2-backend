package pt.uc.dei.proj2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.inject.Inject;
import pt.uc.dei.proj2.beans.UtilityBean;
import pt.uc.dei.proj2.utils.State;

import java.time.LocalDate;

public class ProductDto {
    @Inject
    UtilityBean utilityBean;

    private static int counter = 0;
    private int idProduto;
    private String titulo;
    private String descricao;
    private String localizacao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate data;
    private int anuncianteId;
    private String categoria;
    private double preco;
    private String imagemProduto;
    private State stateId;

    public ProductDto() {
        this.idProduto = ++counter;
    }

    public ProductDto(String titulo, String descricao, String localizacao, LocalDate data, int anuncianteId, String categoria, double preco, String imagemProduto, State stateId, int idProduto) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.data = data;
        this.anuncianteId = anuncianteId;
        this.categoria = categoria;
        this.preco = preco;
        this.imagemProduto = imagemProduto;
        this.stateId = stateId;
        this.idProduto = idProduto;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getAnuncianteId() {
        return anuncianteId;
    }

    public void setAnuncianteId(int anuncianteId) {
        this.anuncianteId = anuncianteId;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getImagemProduto() {
        return imagemProduto;
    }

    public void setImagemProduto(String imagemProduto) {
        this.imagemProduto = imagemProduto;
    }

    public int getCounter() {
        return counter;
    }

    public static void setCounter(int newCounter) {
        counter = newCounter;
    }

    public State getStateId() {
        return stateId;
    }

    public void setStateId(State stateId) {
        this.stateId = stateId;
    }
}
