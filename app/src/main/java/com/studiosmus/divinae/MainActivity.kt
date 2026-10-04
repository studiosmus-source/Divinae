package com.studiosmus.divinae

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Terzina(val versi:String,val moderno:String,val nota:String)
private val cantoI=listOf(
 Terzina("Nel mezzo del cammin di nostra vita
     mi ritrovai per una selva oscura,
     ché la diritta via era smarrita.","A metà della vita mi ritrovo in un luogo oscuro: ho perso la strada giusta.","La selva è insieme luogo reale del racconto e immagine dello smarrimento di Dante."),
 Terzina("Ahi quanto a dir qual era è cosa dura
     esta selva selvaggia e aspra e forte
     che nel pensier rinova la paura!","È difficile perfino descrivere quanto quel bosco fosse terribile: ricordarlo fa tornare la paura.","Dante ci fa capire che quell'esperienza continua a scuoterlo anche mentre la racconta."),
 Terzina("Tant’è amara che poco è più morte;
     ma per trattar del ben ch’i’ vi trovai,
     dirò de l’altre cose ch’i’ v’ho scorte.","Fu un'esperienza quasi peggiore della morte; ma racconterò il bene che vi trovai e ciò che vidi.","La promessa è già qui: dentro lo smarrimento Dante troverà qualcosa capace di salvarlo."),
 Terzina("Io non so ben ridir com’i’ v’intrai,
     tant’era pien di sonno a quel punto
     che la verace via abbandonai.","Non so spiegare bene come ci entrai: ero come addormentato quando abbandonai la strada vera.","Il sonno suggerisce inconsapevolezza: ci si può perdere prima ancora di accorgersi di essersi perduti.")
)

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{Divinae()}}
}

@Composable fun Divinae(){
 var selected by remember{mutableStateOf<Terzina?>(null)}
 val night=Color(0xFF120A05);val paper=Color(0xFFE8C98E);val ink=Color(0xFF24140B);val rubric=Color(0xFF8A2619)
 Box(Modifier.fillMaxSize().background(night)){
  Column(Modifier.fillMaxSize()){
   Row(Modifier.fillMaxWidth().height(54.dp).padding(horizontal=18.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween){
    Text("‹",color=Color(0xFFBDAA87),fontSize=28.sp)
    Text("DIVINAE  ·  INFERNO I",color=Color(0xFFBDAA87),fontSize=10.sp,letterSpacing=2.sp)
    Text("⋮",color=Color(0xFFBDAA87),fontSize=24.sp)
   }
   Surface(Modifier.padding(horizontal=8.dp).fillMaxWidth().weight(1f).shadow(28.dp),color=paper,shape=RoundedCornerShape(12.dp)){
    Column(Modifier.fillMaxSize().background(Color(0xFFE8C98E)).verticalScroll(rememberScrollState()).padding(horizontal=24.dp,vertical=26.dp)){
     Text("INFERNO",Modifier.fillMaxWidth(),color=rubric,fontFamily=FontFamily.Serif,fontSize=34.sp,letterSpacing=3.sp)
     Text("CANTO I",Modifier.fillMaxWidth(),color=ink,fontFamily=FontFamily.Serif,fontSize=18.sp,letterSpacing=2.sp)
     Spacer(Modifier.height(16.dp))
     Text("❦  LO SMARRIMENTO  ❦",Modifier.fillMaxWidth(),textAlign=TextAlign.Center,color=Color(0xFF76502C),fontSize=10.sp,letterSpacing=2.4.sp)
     Spacer(Modifier.height(9.dp));Text("❦",Modifier.fillMaxWidth(),textAlign=TextAlign.Center,color=Color(0xFF98713D),fontSize=19.sp);Spacer(Modifier.height(24.dp))
     cantoI.forEachIndexed{i,t->
      Row(Modifier.fillMaxWidth().clickable{selected=t}.padding(vertical=8.dp)){
       if(i==0) Surface(color=Color(0xFF1E4C5A),shape=RoundedCornerShape(3.dp)){ Text("N",Modifier.padding(horizontal=8.dp,vertical=2.dp),color=Color(0xFFD29A32),fontFamily=FontFamily.Serif,fontWeight=FontWeight.Bold,fontSize=58.sp,lineHeight=62.sp) }
       Text(if(i==0)t.versi.drop(1) else t.versi,Modifier.weight(1f).padding(top=if(i==0)5.dp else 0.dp),color=ink,fontFamily=FontFamily.Serif,fontSize=20.sp,lineHeight=31.sp)
      }
      Spacer(Modifier.height(8.dp))
     }
     Text("Tocca una terzina per comprenderla",Modifier.fillMaxWidth().padding(top=10.dp),textAlign=TextAlign.Center,color=Color(0xFF8E785C),fontSize=10.sp,letterSpacing=1.sp)
    }
   }
   Spacer(Modifier.height(12.dp))
  }
  AnimatedVisibility(selected!=null,Modifier.align(Alignment.BottomCenter),enter=slideInVertically{it}+fadeIn(),exit=slideOutVertically{it}+fadeOut()){
   Surface(Modifier.padding(horizontal=12.dp,vertical=10.dp).fillMaxWidth().clickable{},color=Color(0xFFE6C78D),shadowElevation=28.dp,shape=RoundedCornerShape(10.dp)){
    Column(Modifier.padding(24.dp).navigationBarsPadding()){
     Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){
      Text("COMPRENDERE I VERSI",color=Color(0xFF765238),fontSize=10.sp,letterSpacing=2.sp,fontWeight=FontWeight.Bold)
      Text("×",Modifier.clickable{selected=null}.padding(8.dp),fontSize=28.sp,color=Color(0xFF765238))
     }
     Spacer(Modifier.height(12.dp));Text("IN PAROLE DI OGGI",color=rubric,fontSize=10.sp,letterSpacing=1.6.sp,fontWeight=FontWeight.Bold)
     Spacer(Modifier.height(7.dp));Text(selected?.moderno?:"",color=ink,fontFamily=FontFamily.Serif,fontSize=18.sp,lineHeight=27.sp)
     Spacer(Modifier.height(18.dp));HorizontalDivider(color=Color(0xFF9C815A).copy(alpha=.35f));Spacer(Modifier.height(16.dp))
     Text("PERCHÉ CONTA",color=rubric,fontSize=10.sp,letterSpacing=1.6.sp,fontWeight=FontWeight.Bold);Spacer(Modifier.height(7.dp))
     Text(selected?.nota?:"",color=ink,fontFamily=FontFamily.Serif,fontSize=15.sp,lineHeight=23.sp)
    }
   }
  }
 }
}