tag @s add leader_ceremony

effect give @s minecraft:darkness 23 10 true
effect give @s alexsmobs:fear 23 1 true

title @s subtitle "''Starclan hails you now by your new name''"
title @s title ""

playsound minecraft:block.beacon.deactivate ambient @s ~ ~ ~ 1 0.8
playsound minecraft:block.portal.travel ambient @s ~ ~ ~ 0.2 1.5
particle minecraft:enchant ~ ~1 ~ 1 1 1 10 40000

schedule function warriorcats:leaderslives_2 5s

