### Changelog

#### Dependencies

-Requires Fabric API version `0.76.0+1.19.2` or greater.

-Requires Data Attributes version `1.4.6+1.19.2` or greater.

-Now comes embedded (jar-in-jar) with [Projectile Damage](https://github.com/ZsoltMolnarrr/ProjectileDamage) `3.2.1+1.19-fabric`.

#### Game Play

+Added client side config option to disable inventory tabs but still allow hotkey access. This is superseded by the server side config option to disable the attributes screen.

-Removed Ranged Damage attribute.

+Added support for [Projectile Damage](https://github.com/ZsoltMolnarrr/ProjectileDamage).

*Attribute screen text is now a bit smaller by default.

+Added localisation for skill/level sounds.

+Updated chinese translation (thanks [Annijang](https://github.com/Annijang)).

*Reworked reset on death config option: is now a percentage slider (0 - 100); 0 means all attributes/skill points/refund points/levels are reset to their defaults on death; 100 means all attributes/skill points/refund points/levels are kept on death; and any percentage in between is equivalent to the percentage of attributes/skill points/refund points/levels that are kept on death. For example, 30% means you will only keep 30% of the aforementioned on death.

*Command `/playerex reset <player>` now adheres to the current reset on death config option.

+Added command `/playerex reset_all`: this applies the command `/playerex reset <player>` to all currently online players. Note that to reset offline players *whilst they are offline* you will still need to manually edit their NBT data.

*Modified command `/playerex resetChunk` to be `/playerex reset_chunk`.

*Modified command `/playerex refundAttribute` to be `/playerex refund_attribute`.

*Modified command `/playerex skillAttribute` to be `/playerex skill_attribute`

*Lots of minor performance tweaks.