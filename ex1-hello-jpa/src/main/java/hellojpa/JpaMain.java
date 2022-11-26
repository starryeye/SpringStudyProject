package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
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
            member.setUsername("hello");
            member.setTeam(team);

            entityManager.persist(member);


            entityManager.flush();
            entityManager.clear();

            Member m = entityManager.getReference(Member.class, member.getId()); //member Proxy, team Proxy


            System.out.println("=======1=======");
            m.getUsername();
            System.out.println("=======2=======");
            m.getTeam().getName();
            System.out.println("=======3=======");

            entityManager.flush();
            entityManager.clear();

            m = entityManager.find(Member.class, member.getId()); //member Entity, team proxy

            System.out.println("m = " + m.getTeam().getClass());

            System.out.println("=======4=======");
            m.getTeam().getName();
            System.out.println("=======5=======");

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            entityManager.close();
        }

        emf.close();
    }

    private static void printMember(Member member) {
        System.out.println("member = " + member.getUsername());
    }

    private static void printMemberAndTeam(Member member) {

        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team = " + team.getName());
    }
}
