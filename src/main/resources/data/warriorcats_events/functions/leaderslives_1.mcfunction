tag @s add leader_ceremony

effect give @s minecraft:darkness 23 10 true
effect give @s warriorcats_events:numb 23 1 true

title @s subtitle "''Starclan hails you now by your new name''"
title @s title ""

execute as @s at @s run playsound minecraft:block.beacon.deactivate ambient @s ~ ~ ~ 1 0.8
execute as @s at @s run playsound minecraft:block.portal.travel ambient @s ~ ~ ~ 0.2 1.5
execute as @s at @s run particle minecraft:enchant ~ ~1 ~ 1 1 1 10 40000 force

schedule function warriorcats_events:leaderslives_2 5s

