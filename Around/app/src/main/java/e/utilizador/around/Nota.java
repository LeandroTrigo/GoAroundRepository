package e.utilizador.around;

import java.util.Date;

public class Nota {

    private String Titulo;
    private String Data;

    Nota(){

    }

    Nota(String Titulo,String Data){
        this.Titulo = Titulo;
        this.Data = Data;
    }

    public String getTitulo(){
        return Titulo;
    }

    public  void setTitulo(String Titulo){
        this.Titulo = Titulo;
    }

    public String getData(){
        return Data;
    }

    public  void setData(String Data){
        this.Data = Data;
    }
}
