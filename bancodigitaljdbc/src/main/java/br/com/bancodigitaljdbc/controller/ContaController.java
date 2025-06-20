package br.com.bancodigitaljdbc.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import br.com.bancodigitaljdbc.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.bancodigitaljdbc.service.ContaService;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    // POST /contas - Criar nova conta
    @PostMapping
    public ResponseEntity<Void> criarConta(@RequestBody ContaDTO dto) throws SQLException {
        contaService.criarConta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // GET /contas/{id} - Obter detalhes de uma conta
    @GetMapping("/{id}")
    public ResponseEntity<ContaDTO> buscarConta(@PathVariable Long id) throws SQLException {
        return ResponseEntity.ok(contaService.buscarContaPorId(id));
    }

    // GET /contas/{id}/saldo - Consultar saldo
    @GetMapping("/{id}/saldo")
    public ResponseEntity<BigDecimal> consultarSaldo(@PathVariable Long id) throws SQLException {
        return ResponseEntity.ok(contaService.consultarSaldo(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ContaDTO>> listarContasDoCliente(
            @PathVariable Long clienteId) throws SQLException {

        List<ContaDTO> contas = contaService.buscarContasPorClienteId(clienteId);

        if (contas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(contas);
    }
    
    // GET /contas/{id}/extrato - Gerar extrato da conta
    @GetMapping("/{id}/extrato")
    public ResponseEntity<String> gerarExtrato(@PathVariable Long id) throws SQLException {
        String extrato = contaService.gerarExtrato(id);
        return ResponseEntity.ok(extrato);
    }

    @GetMapping("/clientes/{id}/extratos")
    public ResponseEntity<String> extratosCliente(@PathVariable Long id) throws SQLException {
        String extratos = contaService.gerarExtratosCliente(id);
        return ResponseEntity.ok(extratos);
    }

    // POST /contas/{id}/deposito
    @PostMapping("/{id}/deposito")
    public ResponseEntity<Void> depositar(@PathVariable Long id, @RequestBody DepositoDTO request) throws SQLException {
        contaService.depositar(id, request.valor());
        return ResponseEntity.ok().build();
    }

    // POST /contas/{id}/saque - Realizar saque
    @PostMapping("/{id}/saque")
    public ResponseEntity<Void> sacar(@PathVariable Long id, @RequestBody ValorRequest request) throws SQLException {
        contaService.sacar(id, request.valor());
        return ResponseEntity.ok().build();
    }

    // POST /contas/{id}/transferencia - Transferência para outra conta
    @PostMapping("/{id}/transferencia")
    public ResponseEntity<Void> transferir(@PathVariable Long id, @RequestBody TransferenciaDTO dto) throws SQLException {
        contaService.transferir(id, dto.destinoId(), dto.valor());
        return ResponseEntity.ok().build();
    }
    
    // POST /contas/{id}/pix - Pagamento via Pix
    @PostMapping("/{id}/pix")
    public ResponseEntity<Void> pagarPix(@PathVariable Long id, @RequestBody PixRequest request) throws SQLException {
        contaService.realizarPix(id, request.chavePix(), request.valor());
        return ResponseEntity.ok().build();
    }

    // PUT /contas/{id} - Atualizar dados da conta
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarConta(@PathVariable Long id, @RequestBody AtualizacaoContaDTO dto) throws SQLException {
        contaService.atualizarConta(id, dto);
        return ResponseEntity.ok().build();
    }

    // PUT /contas/{id}/manutencao - Aplicar taxa de manutenção
    @PutMapping("/{id}/manutencao")
    public ResponseEntity<Void> aplicarManutencao(@PathVariable Long id) throws SQLException {
        contaService.aplicarTaxaManutencao(id);
        return ResponseEntity.ok().build();
    }

    // PUT /contas/{id}/rendimentos - Aplicar rendimento mensal (se quiser implementar também)
    @PutMapping("/{id}/rendimentos")
    public ResponseEntity<Void> aplicarRendimento(@PathVariable Long id) throws SQLException {
        contaService.aplicarRendimento(id);
        return ResponseEntity.ok().build();
    }

    // DELETE /contas/{id} - Deletar conta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConta(@PathVariable Long id) throws SQLException {
        contaService.deletarConta(id);
        return ResponseEntity.noContent().build(); // 204
    }

}
