package com.finalproject.ildoduk.entity.auction;

import com.finalproject.ildoduk.entity.member.Member;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.*;
import java.time.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "user") //스택 오버플로 방지
@EntityListeners(AuditingEntityListener.class) //regDate 입력 위함
@DynamicInsert
public class AuctionList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aucSeq;

    @ManyToOne(fetch = FetchType.LAZY) //성능향상을 위해 지연로딩, 필요할 때만 아우터조인 사용
    private Member user;

    @Column(nullable = false)
    private int category;

    @Column(length = 300,nullable = true)
    private String address;

    @Column(length = 30,nullable = true)
    private String sido;

    @Column(length = 30,nullable = true)
    private String sigungu;

    private LocalDateTime doDateTime;

    @Column(length = 30,nullable = false)
    private String predictHour;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private int startPrice;

    @Column(nullable = false)
    private int auctionGap;

    @Column(length = 1000,nullable = false)
    private String title;

    @Column(length = 3000,nullable = false)
    private String content;

    @CreatedDate
    private LocalDateTime regDate;

    @ColumnDefault(value = "0")
    private int driverLicense;
    @ColumnDefault(value = "0")
    private int gender;
    @ColumnDefault(value = "0")
    private int age;
    @ColumnDefault(value = "0")
    private int state;

    public void changeState(int state){
        this.state = state;
    }

}
