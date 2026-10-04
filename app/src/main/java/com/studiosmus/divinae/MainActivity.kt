package com.studiosmus.divinae

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Terzina(val versi:String,val moderno:String,val nota:String)
private val cantoI=listOf(
 Terzina("""Nel mezzo del cammin di nostra vita
mi ritrovai per una selva oscura,
ché la diritta via era smarrita.""","A metà della vita mi ritrovo in un luogo oscuro: ho perso la strada giusta.","La selva è insieme luogo reale del racconto e immagine dello smarrimento di Dante."),
 Terzina("""Ahi quanto a dir qual era è cosa dura
esta selva selvaggia e aspra e forte
che nel pensier rinova la paura!""","È difficile perfino descrivere quanto quel bosco fosse terribile: ricordarlo fa tornare la paura.","Dante ci fa capire che quell'esperienza continua a scuoterlo anche mentre la racconta."),
 Terzina("""Tant’è amara che poco è più morte;
ma per trattar del ben ch’i’ vi trovai,
dirò de l’altre cose ch’i’ v’ho scorte.""","Fu un'esperienza quasi peggiore della morte; ma racconterò il bene che vi trovai e ciò che vidi.","La promessa è già qui: dentro lo smarrimento Dante troverà qualcosa capace di salvarlo."),
 Terzina("""Io non so ben ridir com’i’ v’intrai,
tant’era pien di sonno a quel punto
che la verace via abbandonai.""","Non so spiegare come ci entrai: ero come addormentato quando abbandonai la via vera.","Il sonno indica inconsapevolezza: Dante si accorge dello smarrimento quando è già dentro la selva.")
)
private val Dante=FontFamily(Font(R.font.im_fell_english))
private val Explain=FontFamily(Font(R.font.eb_garamond))
private val Ink=Color(0xFF2B190F); private val Red=Color(0xFF7D1F18); private val Gold=Color(0xFFC59A45)

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){
  enableEdgeToEdge(SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),SystemBarStyle.dark(android.graphics.Color.TRANSPARENT))
  super.onCreate(savedInstanceState); setContent{MaterialTheme{Divinae()}}
 }
}

@Composable fun Divinae(){
 var page by rememberSaveable{mutableIntStateOf(0)}
 var selected by rememberSaveable{mutableStateOf<Int?>(null)}
 Box(Modifier.fillMaxSize().background(Color(0xFF100806))){
  Image(painterResource(R.drawable.manuscript_frame),null,Modifier.fillMaxSize(),contentScale=ContentScale.FillBounds)
  Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(start=68.dp,end=64.dp,top=52.dp,bottom=126.dp),horizontalAlignment=Alignment.CenterHorizontally){
   Text("INFERNO",fontFamily=Explain,fontWeight=FontWeight.Bold,fontSize=13.sp,letterSpacing=3.sp,color=Red)
   Text("CANTO I",fontFamily=Explain,fontWeight=FontWeight.Bold,fontSize=22.sp,color=Ink)
   Box(Modifier.padding(vertical=7.dp).width(84.dp).height(1.dp).background(Gold.copy(.7f)))
   Column(Modifier.weight(1f).verticalScroll(rememberScrollState())){
    val range=if(page==0) 0..1 else 2..3
    range.forEach{i-> val t=cantoI[i]; val active=selected==i
     Column(Modifier.fillMaxWidth().padding(vertical=5.dp).clip(RoundedCornerShape(4.dp)).background(if(active) Gold.copy(.20f) else Color.Transparent).clickable{selected=i}.padding(5.dp)){
      Text("${i*3+1}",fontFamily=Explain,fontSize=10.sp,color=Red.copy(.75f))
      Text(t.versi,fontFamily=Dante,fontSize=18.sp,lineHeight=24.sp,color=Ink)
     }
    }
   }
   Text("Tocca una terzina per comprenderla",fontFamily=Explain,fontStyle=FontStyle.Italic,fontSize=11.sp,color=Ink.copy(.68f))
   Row(Modifier.fillMaxWidth().padding(top=7.dp),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){
    Text("‹",Modifier.clickable(enabled=page>0){page--;selected=null}.padding(8.dp),fontFamily=Dante,fontSize=30.sp,color=if(page>0) Red else Ink.copy(.2f))
    Text("${page+1} / 2",fontFamily=Explain,fontSize=12.sp,color=Ink)
    Text("›",Modifier.clickable(enabled=page<1){page++;selected=null}.padding(8.dp),fontFamily=Dante,fontSize=30.sp,color=if(page<1) Red else Ink.copy(.2f))
   }
  }
  EdgeTabs(Modifier.align(Alignment.CenterEnd))
  AnimatedVisibility(selected!=null,enter=fadeIn(),exit=fadeOut(),modifier=Modifier.align(Alignment.BottomCenter)){selected?.let{Explanation(it,cantoI[it]){selected=null}}}
 }
}

@Composable private fun EdgeTabs(modifier:Modifier=Modifier){
 var open by rememberSaveable{mutableStateOf<String?>(null)}
 val items=listOf("♫" to "Musica","🔥" to "Atmosfera","☰" to "Indice")
 Column(modifier.padding(end=2.dp),verticalArrangement=Arrangement.spacedBy(10.dp),horizontalAlignment=Alignment.End){
  items.forEach{(icon,label)->
   Row(verticalAlignment=Alignment.CenterVertically){
    AnimatedVisibility(open==label){
     Surface(color=Color(0xFF3A2117).copy(.97f),shape=RoundedCornerShape(10.dp)){
      Column(Modifier.width(174.dp).padding(14.dp)){
       Text(label,fontFamily=Explain,fontSize=18.sp,color=Color(0xFFF0DDAF))
       val body=when(label){"Musica"->"▶  Musica medievale\nVolume   ━━━━━";"Atmosfera"->"🔥 Camino   ON\n🌲 Selva    ON\nVolume   ━━━━━";else->"INFERNO\nCanto I · La selva oscura\n\nPURGATORIO\nPARADISO"}
       Text(body,fontFamily=Explain,fontSize=14.sp,lineHeight=21.sp,color=Color(0xFFE2C88E),modifier=Modifier.padding(top=8.dp))
      }
     }
    }
    Text(icon,Modifier.clip(RoundedCornerShape(topStart=8.dp,bottomStart=8.dp)).background(Color(0xFF3A2117).copy(.95f)).clickable{open=if(open==label)null else label}.padding(horizontal=9.dp,vertical=12.dp),fontSize=17.sp,color=Gold)
   }
  }
 }
}

@Composable private fun Explanation(index:Int,t:Terzina,onClose:()->Unit){
 Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(start=28.dp,end=28.dp,bottom=18.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF0DDAF).copy(.98f)).clickable{onClose()}.padding(horizontal=20.dp,vertical=14.dp)){
  Text("VERSI ${index*3+1}–${index*3+3}",fontFamily=Explain,fontWeight=FontWeight.Bold,fontSize=10.sp,letterSpacing=1.5.sp,color=Red)
  Text("In parole di oggi",fontFamily=Explain,fontWeight=FontWeight.Bold,fontSize=16.sp,color=Ink,modifier=Modifier.padding(top=6.dp))
  Text(t.moderno,fontFamily=Explain,fontSize=15.sp,lineHeight=20.sp,color=Ink)
  Text("Perché conta",fontFamily=Explain,fontWeight=FontWeight.Bold,fontSize=14.sp,color=Red,modifier=Modifier.padding(top=8.dp))
  Text(t.nota,fontFamily=Explain,fontSize=13.sp,lineHeight=18.sp,color=Ink.copy(.88f))
  Text("tocca per chiudere",fontFamily=Explain,fontStyle=FontStyle.Italic,fontSize=10.sp,color=Ink.copy(.55f),textAlign=TextAlign.End,modifier=Modifier.fillMaxWidth().padding(top=5.dp))
 }
}
