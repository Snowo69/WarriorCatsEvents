execute as @a[tag=leader_ceremony] run title @s actionbar {"text":"as a new power burns in you","color":"gray","italic":true}
execute as @a[tag=leader_ceremony] run title @s subtitle {"text":"You feel your life ebbing away","color":"gray","italic":true}
execute as @a[tag=leader_ceremony] run title @s title ""
execute as @a[tag=leader_ceremony] run particle minecraft:enchant ~ ~ ~ 1 1 1 80 40000

 execute as @a[tag=leader_ceremony] run schedule function warriorcats_events:livesound_1 3s append
 execute as @a[tag=leader_ceremony] run schedule function warriorcats_events:livesound_1 4s append
 execute as @a[tag=leader_ceremony] run schedule function warriorcats_events:livesound_1 5s append
 execute as @a[tag=leader_ceremony] run schedule function warriorcats_events:livesound_1 6s append
 execute as @a[tag=leader_ceremony] run schedule function warriorcats_events:livesound_1 7s append
 execute as @a[tag=leader_ceremony] run schedule function warriorcats_events:livesound_1 8s append
 execute as @a[tag=leader_ceremony] run schedule function warriorcats_events:livesound_1 9s append
 execute as @a[tag=leader_ceremony] run schedule function warriorcats_events:livesound_1 10s append
 execute as @a[tag=leader_ceremony] run schedule function warriorcats_events:livesound_1 11s append

schedule function warriorcats_events:leaderslives_3 13s