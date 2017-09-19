package Classes;


public class Avaliacao {
    private String titulo;
    private String usuario;
    private String avaliacao;
    private int nota_final;
    
    public Avaliacao(){
        
    }
    
    public Avaliacao(String titulo, String ususario, String avaliacao, float nota_final ){
        this.titulo = titulo;
        this.usuario = ususario;
        this.avaliacao = avaliacao;
        this.nota_final = (int) nota_final;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public float getNota_final() {
        return nota_final;
    }

    public void setNota_final(float nota_final) {
        this.nota_final = (int) nota_final;
    }
    
}
