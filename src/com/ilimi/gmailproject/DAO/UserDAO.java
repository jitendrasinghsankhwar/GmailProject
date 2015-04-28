package com.ilimi.gmailproject.DAO;

import java.util.List;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.springframework.stereotype.Component;

import com.ilimi.gmailproject.entity.UserEntity;

@Component
public class UserDAO {

	public void insertUserInfo(UserEntity user) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		session.save(user);
		session.getTransaction().commit();
		session.close();
	}

	public boolean fetchUserData(String userEmail) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List users = (List) session.createQuery("FROM UserEntity").list();
			for (Iterator iterator = ((java.util.List) users).iterator(); iterator.hasNext();) {
				UserEntity user = (UserEntity) iterator.next();

				if (user.getEmail().equals(userEmail)) {
					return true;
				}

			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return false;
	}

}
