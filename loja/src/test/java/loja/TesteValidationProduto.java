package loja;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.unibh.loja.entidades.Categoria;
import br.unibh.loja.entidades.Cliente;
import br.unibh.loja.entidades.Produto;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TesteValidationProduto {
	private static Validator validator;

	@BeforeClass
	public static void setUp() {
		System.out.println("Inicializando validador...");
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testeValidacaoCliente1() {
		Categoria cat = new Categoria(1L, "Frutas - Produtos frutas");

		Produto p = new Produto(1L, "Maçã", "Uma maçã parecida com a da marca apple", cat, new BigDecimal("15"),
				"Apple");
		Set<ConstraintViolation<Produto>> constraintViolations = validator.validate(p);
		for (ConstraintViolation<Produto> m : constraintViolations) {
			System.out.println(" Erro de Validacao: " + m.getMessage());
		}
		Assert.assertEquals(0, constraintViolations.size());
	}

	@Test
	public void testeValidacaoCliente2() {
		Categoria cat = new Categoria(1L, "Frutas - Produtos frutas");

		Produto p = new Produto(1L, "Maçã!", "Uma maçã parecida com a da marca apple!", cat, new BigDecimal("-5"),
				"Apple!");
		Set<ConstraintViolation<Produto>> constraintViolations = validator.validate(p);
		for (ConstraintViolation<Produto> m : constraintViolations) {
			System.out.println(" Erro de Validacao: " + m.getMessage());
		}
		Assert.assertEquals(4, constraintViolations.size());
	}
}
