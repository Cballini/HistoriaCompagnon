package com.rp.historiacompagnon.entity

import com.rp.historiacompagnon.enum.SkillNameEnum

class Skill(var name: SkillNameEnum, var mastery: Boolean) {

    constructor(): this(SkillNameEnum.DEFAULT, false)
}