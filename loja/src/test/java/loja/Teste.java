package loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;

import org.junit.Test;

import br.unibh.loja.entidades.Categoria;
import br.unibh.loja.entidades.Cliente;
import br.unibh.loja.entidades.Produto;

public class Teste {

	
	@Test
	public void testCreateObject() {
		Categoria cat = new Categoria(1L, "fruta");
		assertEquals(cat.getId(), new Long(1));
		
		Cliente cli = new Cliente(1L, "Sandro", "smt", "senhasegura", "smt", "11241844658", "317070707070",
				"sandro@email.com", Date.from(Instant.now()), Date.from(Instant.now()) );
		assertEquals(cli.getId(), new Long(1));
		
		Produto prod = new Produto(1L, "maçã", "uma linda maçã", cat, new BigDecimal(15), "árvore");
		assertEquals(prod.getId(), new Long(1));
	}
	
	@Test
	public void testCompareObjects() {
		Categoria cat1 = new Categoria(1L, "fruta");
		Categoria cat2 = new Categoria(1L, "fruta");
		assertNotEquals(cat1, cat2);
		
		Cliente cli1 = new Cliente(1L, "Sandro", "smt", "senhasegura", "smt", "11241844658", "317070707070",
				"sandro@email.com", Date.from(Instant.now()), Date.from(Instant.now()) );
		Cliente cli2 = new Cliente(1L, "Sandro", "smt", "senhasegura", "smt", "11241844658", "317070707070",
				"sandro@email.com", Date.from(Instant.now()), Date.from(Instant.now()) );
		assertNotEquals(cli1, cli2);
		
		Produto prod1 = new Produto(1L, "maçã", "uma linda maçã", cat1, new BigDecimal(15), "árvore");
		Produto prod2 = new Produto(1L, "maçã", "uma linda maçã", cat1, new BigDecimal(15), "árvore");
		assertNotEquals(prod1, prod2);
	}
	
	@Test
	public void testGenerateHash() {
		Categoria cat1 = new Categoria(1L, "fruta");
		Categoria cat2 = new Categoria(1L, "fruta");
		assertNotEquals(cat1.hashCode(), cat2.hashCode());
		
		Cliente cli1 = new Cliente(1L, "Sandro", "smt", "senhasegura", "smt", "11241844658", "317070707070",
				"sandro@email.com", Date.from(Instant.now()), Date.from(Instant.now()) );
		Cliente cli2 = new Cliente(1L, "Sandro", "smt", "senhasegura", "smt", "11241844658", "317070707070",
				"sandro@email.com", Date.from(Instant.now()), Date.from(Instant.now()) );
		assertNotEquals(cli1.hashCode(), cli2.hashCode());
		
		Produto prod1 = new Produto(1L, "maçã", "uma linda maçã", cat1, new BigDecimal(15), "árvore");
		Produto prod2 = new Produto(1L, "maçã", "uma linda maçã", cat1, new BigDecimal(15), "árvore");
		assertNotEquals(prod1.hashCode(), prod2.hashCode());
	}
}
