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
            Team team = new Team();
            team.setName("teamA");
            entityManager.persist(team);

            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);

            member.changeTeam(team);

            entityManager.persist(member);




            entityManager.flush();
            entityManager.clear();

            List<Member> result = entityManager.createQuery("select m from Member m inner join m.team t", Member.class)
                            .getResultList();

            System.out.println("result = " + result.size());
            for (Member member1 : result) {
                System.out.println("member1 = " + member1);
            }

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
