package loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.joda.time.DateTime;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableInstant;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import br.unibh.loja.entidades.Cliente;
import br.unibh.loja.negocio.DAO;
import br.unibh.loja.negocio.ServicoCliente;
import br.unibh.loja.util.Resources;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TesteServicoCliente {

	@Deployment
	public static Archive<?> createTestArchive() {
		// Cria o pacote que vai ser instalado no Wildfly para realizacao dos testes
		return ShrinkWrap.create(WebArchive.class, "testeloja.war")
				.addClasses(Cliente.class, Resources.class, DAO.class, ServicoCliente.class,
						TesteServicoCliente.class, ReadableInstant.class, DateTime.class, ReadableDateTime.class)
				.addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	// Realiza as injecoes com CDI
	@Inject
	private Logger log;
	@Inject
	private ServicoCliente sc;

	@Test
	public void teste01_inserirSemErro() throws Exception {
		log.info("============> Iniciando o teste " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Calendar cal = Calendar.getInstance();
		cal.set(1996, 11, 9);
		Date nasc = cal.getTime();
		cal.set(2018, 5, 9);
		Date cadastro = cal.getTime();
		

		Cliente c = new Cliente(1L, "Sandro", "smtjunior", "1234", "Standart", "11241844658", "(31)81239-3228",
				"jniorsandro@gmail.com", nasc, cadastro);
		sc.insert(c);
		Cliente aux = (Cliente) sc.findByName("Sandro").get(0);
		assertNotNull(aux);
		log.info("============> Finalizando o teste " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	@Test
	public void teste02_inserirComErro() throws Exception {
		log.info("============> Iniciando o teste " + Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			Calendar cal = Calendar.getInstance();
			cal.set(1996, 11, 9);
			Date nasc = cal.getTime();
			cal.set(2018, 5, 9);
			Date cadastro = cal.getTime();

			Cliente c = new Cliente(2L, "Joao", "euloginaaaa", "1234", "Standart", "11241844659", "(31)81239-3228",
					"jniorsandro@gmail.com", nasc, cadastro);
			sc.insert(c);
		} catch (Exception e) {
			assertTrue(checkString(e, "CPF Inválido"));
		}
		log.info("============> Finalizando o teste " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	@Test
	public void teste03_atualizar() throws Exception {
		log.info("============> Iniciando o teste " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Cliente c = (Cliente) sc.findByName("Sandro").get(0);
		c.setNome("Sandro Júnior");
		sc.update(c);
		Cliente aux = (Cliente) sc.findByName("Sandro Júnior").get(0);
		assertNotNull(aux);
		log.info("============> Finalizando o teste " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	@Test
	public void teste04_excluir() throws Exception {
		log.info("============> Iniciando o teste " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Cliente c = (Cliente) sc.findByName("Sandro Júnior").get(0);
		sc.delete(c);
		assertEquals(0, sc.findByName("Sandro Júnior").size());
		log.info("============> Finalizando o teste " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	private boolean checkString(Throwable e, String str) {
		if (e.getMessage().contains(str)) {
			return true;
		} else if (e.getCause() != null) {
			return checkString(e.getCause(), str);
		}
		return false;
	}

}
