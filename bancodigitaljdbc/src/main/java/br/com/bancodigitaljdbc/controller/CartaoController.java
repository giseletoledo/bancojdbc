package br.com.bancodigitaljdbc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.bancodigitaljdbc.dto.CartaoAtualizacaoDTO;
import br.com.bancodigitaljdbc.dto.CartaoDTO;
import br.com.bancodigitaljdbc.dto.CartaoPagamentoDTO;
import br.com.bancodigitaljdbc.model.Cartao;
import br.com.bancodigitaljdbc.model.CartaoTransacao;
import br.com.bancodigitaljdbc.service.CartaoService;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    // POST /cartoes - Criar cartão
    @PostMapping
    public ResponseEntity<String> criarCartao(@RequestBody CartaoDTO dto) {
        try {
            cartaoService.criarCartao(dto);
            return ResponseEntity.ok("Cartão criado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar cartão: " + e.getMessage());
        }
    }

    // GET /cartoes/{id} - Detalhes do cartão
    @GetMapping("/{id}")
    public ResponseEntity<Cartao> buscarCartao(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(cartaoService.buscarCartao(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /cartoes/{id}/fatura - Consulta de fatura (simplificado)
    @GetMapping("/{id}/fatura")
    public ResponseEntity<List<CartaoTransacao>> consultarFatura(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(cartaoService.consultarFatura(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /cartoes/{id}/pagamento - Usar cartão
    @PostMapping("/{id}/pagamento")
    public ResponseEntity<Void> realizarPagamento(@PathVariable Long id, @RequestBody CartaoPagamentoDTO dto) {
        try {
            cartaoService.registrarPagamento(id, dto.valor(), dto.descricao());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT /cartoes/{id}/limite - Alterar limite
    @PutMapping("/{id}/limite")
    public ResponseEntity<Void> atualizarLimite(@PathVariable Long id, @RequestBody CartaoAtualizacaoDTO dto) {
        try {
            cartaoService.alterarLimite(id, dto.limite());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT /cartoes/{id}/status - Ativar/Desativar
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            cartaoService.alterarStatus(id, body.get("status"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT /cartoes/{id}/senha - Alterar senha
    @PutMapping("/{id}/senha")
    public ResponseEntity<Void> atualizarSenha(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            cartaoService.alterarSenha(id, body.get("senha"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT /cartoes/{id}/limite-diario - Débito: Atualizar limite diário
    @PutMapping("/{id}/limite-diario")
    public ResponseEntity<Void> atualizarLimiteDiario(@PathVariable Long id, @RequestBody CartaoAtualizacaoDTO dto) {
        try {
            cartaoService.alterarLimiteDiario(id, dto.limiteDiario());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT /cartoes/{id} - Atualizar dados do cartão (ex: tipoCartao, senha, limite, etc.)
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarCartao(@PathVariable Long id, @RequestBody CartaoDTO dto) {
        try {
            cartaoService.atualizarCartao(id, dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE /cartoes/{id} - Deletar cartão
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCartao(@PathVariable Long id) {
        try {
            cartaoService.deletarCartao(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
