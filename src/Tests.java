public class Tests {
    public static void main(String[] args) {
        testMean();
        testMedian();
    }

    public static void testMean() {
        User user = initTestUser();
        var history = user.getHistory();
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

    public static void testMedian() {
        User user = initTestUser();
        var history = user.getHistory();
        assert user.getStatistic(Statistic.MEDIAN) == 1.0;
        history.replace("3", 1);
        assert user.getStatistic(Statistic.MEDIAN) == 0.0;
        history.replace("4", 1);
        assert user.getStatistic(Statistic.MEDIAN) == 0.0;
    }

    public static User initTestUser() {
        User user = new User("test");
        var history = user.getHistory();
        history.put("Rounds", 1);
        history.put("1", 1);
        history.put("2", 1);
        history.put("3", 0);
        history.put("4", 0);
        return user;
    }
}
