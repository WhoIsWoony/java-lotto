package lotto.player;

import static lotto.util.Error.ERROR_INVALID_WINNING_NUMBERS;
import static lotto.util.Rank.RANK1;
import static lotto.util.Rank.RANK2;
import static lotto.util.Rank.RANK3;
import static lotto.util.Rank.RANK4;
import static lotto.util.Rank.RANK5;
import static lotto.util.Rank.RANK_NONE;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lotto.data.Lotto;
import lotto.util.Rank;

public class House {
    private final Lotto win;
    public House(String winnerNumbers, int bonus) {
        List<Integer> numbersInput = convertWinnerNumber(winnerNumbers);
        win = new Lotto(numbersInput, bonus);
    }

    private List<Integer> convertWinnerNumber(String winnerNumbers){
        try{
            String[] splitByComma = winnerNumbers.split(",");
            Stream<String> streamOfStr = Arrays.stream(splitByComma);
            Stream<Integer> streamOfInt = streamOfStr.map(Integer::parseInt);
            return streamOfInt.collect(Collectors.toList());
        }catch (Error e){
            ERROR_INVALID_WINNING_NUMBERS.generate();
        }
        return null;
    }

    public LinkedHashMap<Rank,Integer> getResultOf(User user){
        LinkedHashMap<Rank,Integer> statistic = new LinkedHashMap<>(){{
            put(RANK5, 0);
            put(RANK4, 0);
            put(RANK3, 0);
            put(RANK2, 0);
            put(RANK1, 0);
        }};

        for(Lotto ticket:user.tickets){
            Rank result = win.checkRankOf(ticket);
            if(result == RANK_NONE) continue;
            statistic.put(result, statistic.get(result)+1);
        }

        return statistic;
    }

    public double getRevenueOf(User user){
        int payment = user.getTicketNum() * 1000;
        int amount = 0;
        for(Lotto ticket:user.tickets){
            Rank result = win.checkRankOf(ticket);
            if(result == RANK_NONE) continue;
            amount += result.getMoney();
        }
        return amount / (double)payment * 100;
    }
}
