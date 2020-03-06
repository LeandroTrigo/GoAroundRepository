package e.utilizador.around;

public class Ponto {

    private int IdPonto;
    private String Imagem;
    private String Titulo;
    private String Descricao;
    private double Latitude;
    private double Longitude;
    private int IdUtilizador;

    public Ponto(int idPonto, String imagem, String titulo, String descricao, double latitude, double longitude, int idUtilizador) {
        IdPonto = idPonto;
        Imagem = imagem;
        Titulo = titulo;
        Descricao = descricao;
        Latitude = latitude;
        Longitude = longitude;
        IdUtilizador = idUtilizador;
    }

    public int getIdPonto() {
        return IdPonto;
    }

    public void setIdPonto(int idPonto) {
        IdPonto = idPonto;
    }

    public String getImagem() {
        return Imagem;
    }

    public void setImagem(String imagem) {
        Imagem = imagem;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public int getIdUtilizador() {
        return IdUtilizador;
    }

    public void setIdUtilizador(int idUtilizador) {
        IdUtilizador = idUtilizador;
    }
}
