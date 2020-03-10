package e.utilizador.around;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class PontosModel {

    private int idPonto;
    private int idUser;
    private String imagem;
    private String titulo;
    private String descricao;
    private double latitude;
    private double longitude;


    public PontosModel(int idPonto, int idUser, String imagem, String titulo, String descricao, float latitude, float longitude) {
        this.idPonto = idPonto;
        this.idUser = idUser;
        this.imagem = imagem;
        this.titulo = titulo;
        this.descricao = descricao;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PontosModel(JSONObject obj) throws JSONException {
        this.idPonto = obj.getInt("IdPonto");
        this.idUser = obj.getInt("IdUtilizador");
        this.imagem = obj.getString("Imagem");
        this.titulo = obj.getString("Titulo");
        this.descricao = obj.getString("Descricao");
        this.latitude = obj.getDouble("Latitude");
        this.longitude = obj.getDouble("Longitude");
    }


    public int getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(int idPonto) {
        this.idPonto = idPonto;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



}

