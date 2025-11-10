execute as @a[tag=leader_ceremony] run scoreboard players set @s Lives 9
execute as @a[tag=leader_ceremony] at @s run particle minecraft:enchant ~ ~1.5 ~ 0 0 0 1 1000
execute as @a[tag=leader_ceremony] at @s run playsound minecraft:block.beacon.activate ambient @p ~ ~ ~ 1 0.7

tag @a[tag=leader_ceremony] remove leader_ceremony
