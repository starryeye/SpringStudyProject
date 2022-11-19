package hellojpa;

import javax.persistence.*;


@Entity
@TableGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        table = "my_sequences",
        pkColumnName = "member_seq", allocationSize = 1)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "MEMBER_SEQ_GENERATOR")
    private Long id;

    @Column(name = "name", nullable = false)
    private String username;

    public Member() {} //jpa는 기본생성자 필요

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
