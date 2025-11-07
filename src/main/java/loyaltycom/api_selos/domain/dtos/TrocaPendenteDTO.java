package loyaltycom.api_selos.domain.dtos;

public interface TrocaPendenteDTO {
    Long getId_produto();
    String getCod_troca();
    Integer getQtd_selos();
    Double getValor_restante();
    java.time.LocalDateTime getCriado_em();
    String getNome();
    String getImg();
}

