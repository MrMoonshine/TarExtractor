package UI.Utils

import java.awt.Color
import javax.swing.BorderFactory
import scala.swing.Button

class MyButton(name_i:String) extends Button(name_i) {
  background = consts.stylecolor
  foreground = consts.styleforeg

  border = BorderFactory.createCompoundBorder(
    BorderFactory.createLineBorder(Color.WHITE,5),
    consts.padborder
  )

  peer.setFocusPainted(false)
}
