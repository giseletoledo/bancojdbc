package br.com.bancodigitaljdbc.mapper;


import br.com.bancodigitaljdbc.dto.ClienteDTO;
import br.com.bancodigitaljdbc.dto.EnderecoDTO;
import br.com.bancodigitaljdbc.model.Cliente;
import br.com.bancodigitaljdbc.model.Endereco;
import br.com.bancodigitaljdbc.model.TipoCliente;

public class ClienteMapper {

    public static ClienteDTO toDTO(Cliente cliente) {
        if (cliente == null) return null;

        Endereco endereco = cliente.getEndereco();

        EnderecoDTO enderecoDTO = new EnderecoDTO(
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );

        return new ClienteDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getDataNascimento(),
                enderecoDTO,
                cliente.getTipo().name()
        );
    }

    public static Cliente toEntity(ClienteDTO dto) {
        if (dto == null) return null;

        EnderecoDTO enderecoDTO = dto.endereco();

        Endereco endereco = new Endereco(
                enderecoDTO.rua(),
                enderecoDTO.numero(),
                enderecoDTO.complemento(),
                enderecoDTO.cidade(),
                enderecoDTO.estado(),
                enderecoDTO.cep()
        );

        TipoCliente tipo;
        try {
            tipo = TipoCliente.valueOf(dto.tipoCliente().toUpperCase());
        } catch (Exception e) {
            tipo = TipoCliente.COMUM; // fallback seguro
        }

        Cliente cliente = new Cliente(
                dto.nome(),
                dto.cpf(),
                dto.dataNascimento(),
                endereco,
                tipo
        );

        if (dto.id() != null) {
            cliente.setId(dto.id());
        }

        return cliente;
    }
}
