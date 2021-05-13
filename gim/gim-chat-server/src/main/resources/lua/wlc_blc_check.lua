local configKey = KEYS[1]
local blcKey = KEYS[2]
local wlcKey = KEYS[3]
local sendId = KEYS[4]
local config = redis.call('get', configKey)
if not config then
    return true;
elseif ('2' == config) then
    local result = redis.call("sismember", blcKey, sendId)
    return 0 == result;
elseif ('3' == config) then
    local result = redis.call("sismember", wlcKey, sendId)
    return result
else
    return false
end