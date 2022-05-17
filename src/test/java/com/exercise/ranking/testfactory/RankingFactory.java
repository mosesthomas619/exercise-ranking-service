package com.exercise.ranking.testfactory;


import com.exercise.ranking.model.RankingUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RankingFactory {

    public static List<RankingUser> getRankingUser() {
        List<RankingUser> rankingUserList = new ArrayList<>();
        RankingUser e1 = new RankingUser(1L, 100, LocalDateTime.parse("2022-04-13T17:09:42.411"));
        RankingUser e2 = new RankingUser(2L, 101, LocalDateTime.parse("2022-04-14T17:09:42.411"));
        rankingUserList.add(e1);
        rankingUserList.add(e2);
        return rankingUserList;

    }

}
