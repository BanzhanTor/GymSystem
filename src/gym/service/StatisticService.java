package gym.service;

import gym.dao.CardTypeDAOImpl;
import gym.dao.MemberDAOImpl;
import gym.dao.RecordDAOImpl;
import gym.entity.CardType;
import gym.entity.InOutRecord;
import gym.entity.Member;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 统计业务类：使用多线程并行统计、Map 分组、Math 取整。
 */
public class StatisticService {

    private final MemberDAOImpl memberDAO = new MemberDAOImpl();
    private final CardTypeDAOImpl cardTypeDAO = new CardTypeDAOImpl();
    private final RecordDAOImpl recordDAO = new RecordDAOImpl();

    public int totalMembers() {
        return memberDAO.findAll().size();
    }

    /** 统计各卡类型的会员人数，使用线程池并行分片统计 */
    public Map<String, Integer> countByCardType() {
        final List<Member> members = memberDAO.findAll();
        final Map<Integer, String> typeNames = new HashMap<>();
        for (CardType ct : cardTypeDAO.findAll()) {
            typeNames.put(ct.getId(), ct.getName());
        }

        Map<String, Integer> countMap = new HashMap<>();
        int nThreads = Math.min(4, Math.max(1, members.size()));
        int size = members.size();
        int per = (int) Math.ceil((double) size / nThreads);

        ExecutorService pool = Executors.newFixedThreadPool(nThreads);
        List<Future<Map<String, Integer>>> futures = new ArrayList<>();
        for (int i = 0; i < nThreads && i * per < size; i++) {
            final int from = i * per;
            final int to = Math.min(size, from + per);
            futures.add(pool.submit(() -> {
                Map<String, Integer> part = new HashMap<>();
                for (int k = from; k < to; k++) {
                    String typeName = typeNames.getOrDefault(members.get(k).getCardTypeId(), "未知");
                    part.merge(typeName, 1, Integer::sum);
                }
                return part;
            }));
        }
        for (Future<Map<String, Integer>> f : futures) {
            try {
                f.get().forEach((k, v) -> countMap.merge(k, v, Integer::sum));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pool.shutdown();
        return countMap;
    }

    /** 计算平均停留时长（分钟，四舍五入取整） */
    public long averageStayMinutes() {
        List<InOutRecord> records = recordDAO.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long totalMinutes = 0;
        int count = 0;
        for (InOutRecord r : records) {
            if (r.getEnterTime() == null || r.getLeaveTime() == null || r.getLeaveTime().isEmpty()) {
                continue;
            }
            try {
                Date d1 = sdf.parse(r.getEnterTime());
                Date d2 = sdf.parse(r.getLeaveTime());
                totalMinutes += (d2.getTime() - d1.getTime()) / (1000 * 60);
                count++;
            } catch (Exception ignored) {
            }
        }
        if (count == 0) {
            return 0;
        }
        return Math.round((double) totalMinutes / count);
    }
}