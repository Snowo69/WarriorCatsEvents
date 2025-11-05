execute as @a[tag=leader_ceremony] run title @s title ""
execute as @a[tag=leader_ceremony] run title @s subtitle {"text":"You wake up as a new leader","color":"gray","italic":true}
execute as @a[tag=leader_ceremony] at @s run playsound minecraft:entity.elder_guardian.curse ambient @s ~ ~ ~ 1 0.7
execute as @a[tag=leader_ceremony] at @s run particle minecraft:portal ~ ~0.3 ~ 0 0 0 1 100

schedule function warriorcats:leaderslives_end 2s