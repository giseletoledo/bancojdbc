package br.com.bancodigitaljdbc.dto;


public record ClienteDTO(
    Long id,
    String nome,
    String cpf,
    String dataNascimento,
    EnderecoDTO endereco,
    String tipoCliente
) {

    public ClienteDTO(Long id, String nome, String cpf, String dataNascimento,
                      String tipoCliente, String rua, String numero, String complemento,
                      String cidade, String estado, String cep) {
        this(
            id,
            nome,
            cpf,
            dataNascimento,
            new EnderecoDTO(rua, numero, complemento, cidade, estado, cep),
            tipoCliente
        );
    }
}


