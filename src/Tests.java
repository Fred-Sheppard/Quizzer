public class Tests {
    public static void main(String[] args) {
        testMean();
    }

    public static void testMean() {
        User user = new User("test");
        var history = user.history;
        history.put("1", 1);
        history.put("2", 1);
        history.put("3", 0);
        history.put("4", 0);
        history.put("Rounds", 1);
        assert user.getStatistic(Statistic.MEAN) == 0.5;
        history.replace("1", 0);
        assert user.getStatistic(Statistic.MEAN) == 0.75;
        history.replace("2", 0);
        assert user.getStatistic(Statistic.MEAN) == 1.0;
        for (String key : history.keySet()) {
            history.replace(key, 1);
        }
        assert user.getStatistic(Statistic.MEAN) == 0.0;
    }
}
