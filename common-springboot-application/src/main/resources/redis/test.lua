local startTime=redis.call('get',KEYS[1]);--startTimeKey
while true do
    local now=os.time();--sec
    if now>startTime then
        local offset=(now-startTime)/5;
        redis.call('set',KEYS[3],offset);
        redis.call('setbit',KEYS[2],offset,'1');
    end
    os.execute("sleep 5");
end
