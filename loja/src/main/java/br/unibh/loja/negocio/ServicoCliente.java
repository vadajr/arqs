package br.unibh.loja.negocio;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.joda.time.DateTime;
import org.joda.time.Period;

import br.unibh.loja.entidades.Cliente;

@Stateless
@LocalBean
public class ServicoCliente implements DAO<Cliente, Long> {
	@Inject
	EntityManager em;
	@Inject
	private Logger log;

	public Cliente insert(Cliente t) throws Exception {
		if (!t.getPerfil().equals("Standart")) {
			throw new Exception("Clientes novos precisam estar com o perfil Standart> " + t.getPerfil());
		}
		log.info("Persistindo " + t);
		em.persist(t);
		return t;
	}

	public Cliente update(Cliente t) throws Exception {
		/*DateTime d1 = new DateTime(t.getDataCadastro());
		DateTime d2 = new DateTime(new Date());
		Period per = new org.joda.time.Period(d1, d2);
		if (per.getYears() < 1) {
			if (t.getPerfil().equals("Standart")) {
				throw new Exception("Clientes novos precisam estar com o perfil Standart");
			}
		}

		if (per.getYears() >= 1 && per.getYears() < 5) {
			if (!t.getPerfil().equals("Standart") || !t.getPerfil().equals("Premium")) {
				throw new Exception("Clientes com menos de 5 anos precisam ter o plano Standart ou Premium");
			}
		}

		if (per.getYears() >= 5) {
			if (!t.getPerfil().equals("Standart")
					|| !t.getPerfil().equals("Premium") && !t.getPerfil().equals("Gold")) {
				throw new Exception("Clientes precisam ter o plano Standart, Premium ou Gold.");
			}
		}*/

		LocalDateTime d1 = LocalDateTime.ofInstant(t.getDataCadastro().toInstant(), ZoneId.systemDefault());
		LocalDateTime d2 = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
		long years = d1.until(d2, ChronoUnit.YEARS);
		if (years < 1) {
			if (!t.getPerfil().equals("Standart")) {
				throw new Exception("Clientes novos precisam estar com o perfil Standart");
			}
		}

		if (years >= 1 && years < 5) {
			if (!t.getPerfil().equals("Standart") || !t.getPerfil().equals("Premium")) {
				throw new Exception("Clientes com menos de 5 anos precisam ter o plano Standart ou Premium (" + years + ")");
			}
		}

		if (years >= 5) {
			if (!t.getPerfil().equals("Standart")
					|| !t.getPerfil().equals("Premium") && !t.getPerfil().equals("Gold")) {
				throw new Exception("Clientes precisam ter o plano Standart, Premium ou Gold.");
			}
		}
			
		log.info("Atualizando " + t);
		return em.merge(t);
	}

	public void delete(Cliente t) throws Exception {
		log.info("Removendo " + t);
		Object c = em.merge(t);
		em.remove(c);
	}

	public Cliente find(Long k) throws Exception {
		log.info("Encontrando pela chave " + k);
		return em.find(Cliente.class, k);
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> findAll() throws Exception {
		log.info("Encontrando todos os objetos");
		return em.createQuery("from Cliente").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> findByName(String name) throws Exception {
		log.info("Encontrando o " + name);
		return em.createNamedQuery("Cliente.findByName").setParameter("nome", "%" + name + "%").getResultList();
	}
}
