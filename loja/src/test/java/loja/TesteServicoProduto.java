package loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
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

import br.unibh.loja.entidades.Categoria;
import br.unibh.loja.entidades.Produto;
import br.unibh.loja.negocio.DAO;
import br.unibh.loja.negocio.ServicoCategoria;
import br.unibh.loja.negocio.ServicoProduto;
import br.unibh.loja.util.Resources;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TesteServicoProduto {

	@Deployment
	public static Archive<?> createTestArchive() {
		// Cria o pacote que vai ser instalado no Wildfly para realizacao dos testes
		return ShrinkWrap.create(WebArchive.class, "testeloja.war")
				.addClasses(Produto.class, ServicoProduto.class, Categoria.class, Resources.class, DAO.class, ServicoCategoria.class,
						TesteServicoProduto.class, ReadableInstant.class, DateTime.class, ReadableDateTime.class)
				.addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	// Realiza as injecoes com CDI
	@Inject
	private Logger log;
	@Inject
	private ServicoProduto sp;
	@Inject
	private ServicoCategoria sc;


	@Test
	public void teste01_inserirSemErro() throws Exception {
		log.info("============> Iniciando o teste " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Categoria c = new Categoria(1L, "Bebidas");
		sc.insert(c);
		Produto p = new Produto(1L, "Cerveja", "Uma bebida para adultos", c, new BigDecimal("15"),
				"Kaiser");
		sp.insert(p);
		
		Produto aux = (Produto) sp.findByName("Cerveja").get(0);
		assertNotNull(aux);
		log.info("============> Finalizando o teste " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	@Test
	public void teste02_inserirComErro() throws Exception {
		log.info("============> Iniciando o teste " + Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			Categoria c = (Categoria) sc.findByName("Bebidas").get(0);
			Produto p = new Produto(1L, ":Cerveja", "Uma bebida ruim", c, new BigDecimal("15"),
					"Kaiser");
			sp.insert(p);
		} catch (Exception e) {
			assertTrue(checkString(e, "Caracteres permitidos: letras, espaços, ponto e aspas simples"));
		}
		log.info("============> Finalizando o teste " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	@Test
	public void teste03_atualizar() throws Exception {
		log.info("============> Iniciando o teste " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Produto p = (Produto) sp.findByName("Cerveja").get(0);
		p.setNome("Refrigerante");
		sp.update(p);
		Produto aux = (Produto) sp.findByName("Refrigerante").get(0);
		assertNotNull(aux);
		log.info("============> Finalizando o teste " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	
	@Test
	public void teste04_excluir() throws Exception {
		log.info("============> Iniciando o teste " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Produto p  = (Produto) sp.findByName("Refrigerante").get(0);
		sp.delete(p);
		sc.delete(p.getCategoria());
		
		assertEquals(0, sp.findByName("Refrigerante").size());
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
