execute as @a[tag=leader_ceremony] run title @s actionbar {"text":"as a new power burns in you","color":"gray","italic":true}
execute as @a[tag=leader_ceremony] run title @s subtitle {"text":"You feel your life ebbing away","color":"gray","italic":true}
execute as @a[tag=leader_ceremony] run title @s title ""
execute as @a[tag=leader_ceremony] run particle minecraft:enchant ~ ~ ~ 1 1 1 80 40000

 execute as @a[tag=leader_ceremony] run schedule function warriorcats:livesound_1 3s
 execute as @a[tag=leader_ceremony] run schedule function warriorcats:livesound_2 4s
 execute as @a[tag=leader_ceremony] run schedule function warriorcats:livesound_3 5s
 execute as @a[tag=leader_ceremony] run schedule function warriorcats:livesound_4 6s   
 execute as @a[tag=leader_ceremony] run schedule function warriorcats:livesound_5 7s 
 execute as @a[tag=leader_ceremony] run schedule function warriorcats:livesound_6 8s
 execute as @a[tag=leader_ceremony] run schedule function warriorcats:livesound_7 9s
 execute as @a[tag=leader_ceremony] run schedule function warriorcats:livesound_8 10s 
 execute as @a[tag=leader_ceremony] run schedule function warriorcats:livesound_9 11s 

schedule function warriorcats:leaderslives_3 13s