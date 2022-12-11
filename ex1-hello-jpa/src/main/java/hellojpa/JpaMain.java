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

            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(new Address("city1", "street1", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new AddressEntity("old1", "street", "10001"));
            member.getAddressHistory().add(new AddressEntity("old2", "street", "10002"));


            entityManager.persist(member);

            entityManager.flush();
            entityManager.clear();

            System.out.println("================= Start ====================");
            Member findMember = entityManager.find(Member.class, member.getId());

            //homeCity -> newCity
//            Address a = findMember.getHomeAddress();
//            findMember.setHomeAddress(new Address("newCity", a.getStreet(), a.getZipcode()));

            //치킨을 한식으로
//            findMember.getFavoriteFoods().remove("치킨");
//            findMember.getFavoriteFoods().add("한식");

            //이미 존재하는 old1을 지우고 싶을땐
            //equals를 기본으로 동작한다..
            //쿼리가 update로 되지 않고 delete, insert(2회, 컬랙션에 저장될 모든 요소) 각각 나간다.
            //쓰지말자..
            //findMember.getAddressHistory().remove(new Address("old1", "street", "10001"));
            //findMember.getAddressHistory().add(new Address("newCity1", "street", "10001"));


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
