package org.nlogo.widget

// Property is more convenient to use from Scala than Java, thanks to named and default arguments,
// so we put all of our Properties for this package here - ST 2/23/10

import org.nlogo.util.JCL.JavaList
import java.awt.GridBagConstraints.RELATIVE
import org.nlogo.api.{I18N, Property => P}

object Properties {
  implicit val i18nPrefix = I18N.Prefix("edit")

  val text = JavaList(
    P("text", P.BigString, I18N.gui("text.text")),
    P("fontSize", P.Integer, I18N.gui("text.fontSize")),
    P("transparency", P.Boolean, I18N.gui("text.transparency")),
    P("color", P.Color, I18N.gui("text.color"))
  )
  val swiitch = JavaList(
    P("nameWrapper", P.Identifier, I18N.gui("switch.globalVar"))
  )
  val dummySwitch = JavaList(
    P("name", P.String, I18N.gui("hubnet.tag"))
  )
}
