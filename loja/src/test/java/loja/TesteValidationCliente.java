package loja;

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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TesteValidationCliente {
	private static Validator validator;

	@BeforeClass
	public static void setUp() {
		System.out.println("Inicializando validador...");
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testeValidacaoCliente1() {
		Calendar cal = Calendar.getInstance();
		cal.set(1996, 11, 9);
		Date nasc = cal.getTime();
		cal.set(2018, 5, 9);
		Date cadastro = cal.getTime();

		Cliente c = new Cliente(1L, "Sandro", "smtjunior", "1234", "admin", "11241844658", "(31)81239-3228",
				"jniorsandro@gmail.com", nasc, cadastro);
		Set<ConstraintViolation<Cliente>> constraintViolations = validator.validate(c);
		for (ConstraintViolation<Cliente> m : constraintViolations) {
			System.out.println(" Erro de Validacao: " + m.getMessage());
		}
		Assert.assertEquals(0, constraintViolations.size());
	}

	@Test
	public void testeValidacaoCliente2() {
		Calendar cal = Calendar.getInstance();
		cal.set(1996, 11, 9);
		Date nasc = cal.getTime();
		cal.set(2018, 5, 9);
		Date cadastro = cal.getTime();

		Cliente c = new Cliente(1L, "Sandro, Júnior", "smtjunior @ 09", "", "", "1124184465", "(31)239-3228",
				"jniorsandro@gmail.com", nasc, cadastro);
		Set<ConstraintViolation<Cliente>> constraintViolations = validator.validate(c);
		for (ConstraintViolation<Cliente> m : constraintViolations) {
			System.out.println(" Erro de Validacao: " + m.getMessage());
		}
		Assert.assertEquals(6, constraintViolations.size());
	}
}