package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager entityManager = emf.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            entityManager.persist(member);

            TypedQuery<Member> query = entityManager.createQuery("select m From Member m", Member.class);
            Query query2 = entityManager.createQuery("select m.username, m.age From Member m");

            List<Member> resultList = query.getResultList();

            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }


            TypedQuery<Member> query3 = entityManager.createQuery("select m From Member m where m.username =:username", Member.class);
            query3.setParameter("username", "member1");
            Member singleResult = query3.getSingleResult();
            System.out.println("singleResult = " + singleResult);


            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            entityManager.close();
        }

        emf.close();
    }
}
