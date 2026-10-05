package com.studiosmus.divinae

import android.content.Context
import org.json.JSONObject

data class Canto(val cantica:String,val numero:Int,val titolo:String,val terzine:List<TerzinaV2>)
data class TerzinaV2(val versi:String,val moderno:String,val nota:String)

object CantoRepository {
 fun load(context:Context,path:String="canti/inferno_01.json"):Canto {
  val root=JSONObject(context.assets.open(path).bufferedReader().use{it.readText()})
  val arr=root.getJSONArray("terzine")
  val list=(0 until arr.length()).map{i->arr.getJSONObject(i).let{
   TerzinaV2(it.getString("versi"),it.getString("moderno"),it.getString("nota"))
  }}
  return Canto(root.getString("cantica"),root.getInt("canto"),root.getString("title"),list)
 }
}