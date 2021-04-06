import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xcw.MainApplication;
import com.xcw.constant.QuartzConst;
import com.xcw.controller.DataController;
import com.xcw.entity.QuartzJob;
import com.xcw.entity.User;
import com.xcw.mapper.ParticipantMapper;
import com.xcw.mapper.UserMapper;
import com.xcw.quartz.JobTest;
import com.xcw.utils.QuartzUtil;
import com.xcw.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @class: RedisTest
 * @author: ChengweiXing
 * @description: TODO
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
@Slf4j
public class RedisTest {

    private static final long TIME_INTERVAL = 5000L;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private QuartzUtil quartzUtil;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private ParticipantMapper participantMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DefaultRedisScript script;

    @Autowired
    private DataController dataController;

    @Test
    public void test6() throws InterruptedException {
        QuartzJob quartzJob = new QuartzJob();
        quartzJob.setJobClass(JobTest.class);
        quartzJob.setJobGroupName(QuartzConst.QZ_JOB_GROUP_NAME);
        quartzJob.setTriggerGroupName(QuartzConst.QZ_TRIGGER_GROUP_NAME);
        quartzJob.setStartTime(new Date());
        quartzJob.setSeconds(5);
        quartzJob.setJobName("test");
        quartzJob.setTriggerName("testtrigger");
        //quartzJob.setEndTime(new Date());
        quartzUtil.addJob(quartzJob);
    }

    @Test
    public void test1() throws InterruptedException {
        //System.out.println(redisTemplate.opsForValue().get("sss"));
        Long startOffset = (Long) redisTemplate.execute((RedisCallback<Long>) con ->
                con.bitPos("xcw".getBytes(), true));
        System.out.println(startOffset);
    }

    @Test
    public void test3(){
        User user = new User();
        user.setId(1);
        user.setName("kobe");
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(User::getId,user.getId())
                .set(User::getName,user.getName());
        //update(updateWrapper);
        userMapper.update(user,updateWrapper);
    }

    @Test
    public void test2(){
//        List<User> users = userMapper.selectList(null);
//        users.forEach(System.out::println);
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.gt("id", 1);
//        userMapper.selectList(wrapper).forEach(System.out::println);
        long l = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            redisTemplate.opsForValue().set("key"+i,"value"+i);
        }
        long l1 = System.currentTimeMillis();
        System.out.println("cost time:"+(l1-l));
    }

    @Test
    public void test4(){
        long start = System.currentTimeMillis();
//        HashMap<String, String> map = new HashMap<>();
//        for (int i = 0; i < 10000; i++) {
//            map.put("pipekey"+i,"v"+i);
//        }
//        redisUtil.batchInsertByPipeline(map, 2000);


    }

    @Test
    public void test5(){
        List<String> keys = Arrays.asList("bbb");
        Object a = redisTemplate.execute(script, keys, "10","1","1");
        System.out.println(a);
    }

    @Test
    public void test7(){
        long start=System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            redisTemplate.opsForValue().setBit("eee",i,true);
        }
        long end=System.currentTimeMillis();
        log.info("不使用pipeline消耗时长：{}",(end-start));
    }

    @Test
    public void test8(){
        long start=System.currentTimeMillis();
        redisTemplate.executePipelined((RedisCallback<?>) connection->{
            connection.openPipeline();
            for (int i = 0; i < 1000; i++) {
                redisTemplate.opsForValue().setBit("ddd",i,true);
            }
            return null;
        });
        long end=System.currentTimeMillis();
        log.info("使用pipeline消耗时长：{}",(end-start));
    }
}
