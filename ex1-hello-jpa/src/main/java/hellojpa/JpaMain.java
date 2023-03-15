package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager entityManager = emf.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        try {

            Parent parent = new Parent();
            parent.setName("p1");

            Child child1 = new Child();
            child1.setName("c1");


            System.out.println("==========1===========");
            entityManager.persist(parent);
            System.out.println("==========2===========");

            entityManager.flush();
            entityManager.clear();;

            System.out.println("==========3===========");
            Parent findParent = entityManager.find(Parent.class, parent.getId());
            System.out.println("==========4===========");

            findParent.addChild(child1);



            System.out.println("==========5===========");

            tx.commit();

            System.out.println("==========6===========");
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            entityManager.close();
        }

        emf.close();
    }
}
