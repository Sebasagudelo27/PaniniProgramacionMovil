package com.example.panini.wc2026.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.panini.wc2026.data.dao.LaminaDao
import com.example.panini.wc2026.data.entity.Lamina
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [Lamina::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun laminaDao(): LaminaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "panini_wc2026.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    precargarLaminas(database.laminaDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun precargarLaminas(dao: LaminaDao) {
            val laminas = mutableListOf<Lamina>()
            var id = 1

            // ── SECCIÓN: PORTADA Y ESPECIALES ──
            val especiales = listOf(
                "Portada del Álbum", "Logo FIFA World Cup 2026", "Trofeo Copa del Mundo",
                "Estadio MetLife (NY)", "Estadio Azteca (México)", "Estadio Rose Bowl (LA)",
                "Estadio AT&T (Dallas)", "Estadio Levi's (SF)", "Estadio Hard Rock (Miami)",
                "Estadio Lincoln Financial (Filadelfia)"
            )
            especiales.forEach { nombre ->
                laminas.add(Lamina(id++, "ESP-${id-1}", nombre, "Especial", "ESPECIALES"))
            }

            // ── GRUPO A: QATAR ──
            agregarJugadores(laminas, id, "QAT", "Qatar", "GRUPO A", listOf(
                "Meshaal Barsham","Salah Zakaria","Abdelkarim Hassan","Bassam Al-Rawi",
                "Pedro Miguel","Assim Madibo","Karim Boudiaf","Ismaeel Mohammad",
                "Hassan Al-Haydos","Akram Afif","Almoez Ali"
            )).also { id = it }

            // ── GRUPO A: ECUADOR ──
            agregarJugadores(laminas, id, "ECU", "Ecuador", "GRUPO A", listOf(
                "Hernan Galindez","Piero Hincapie","Felix Torres","Diego Palacios",
                "Angelo Preciado","Jhegson Mendez","Carlos Gruezo","Jeremy Sarmiento",
                "Gonzalo Plata","Enner Valencia","Michael Estrada"
            )).also { id = it }

            // ── GRUPO A: SENEGAL ──
            agregarJugadores(laminas, id, "SEN", "Senegal", "GRUPO A", listOf(
                "Edouard Mendy","Kalidou Koulibaly","Abdou Diallo","Youssouf Sabaly",
                "Idrissa Gueye","Nampalys Mendy","Pape Gueye","Ismaila Sarr",
                "Sadio Mane","Boulaye Dia","Famara Diedhiou"
            )).also { id = it }

            // ── GRUPO A: PAÍSES BAJOS ──
            agregarJugadores(laminas, id, "NED", "Países Bajos", "GRUPO A", listOf(
                "Andries Noppert","Virgil van Dijk","Matthijs de Ligt","Denzel Dumfries",
                "Daley Blind","Frenkie de Jong","Steven Berghuis","Cody Gakpo",
                "Memphis Depay","Davy Klaassen","Vincent Janssen"
            )).also { id = it }

            // ── GRUPO B: ENGLAND ──
            agregarJugadores(laminas, id, "ENG", "Inglaterra", "GRUPO B", listOf(
                "Jordan Pickford","Kyle Walker","Harry Maguire","Luke Shaw",
                "Trent Alexander-Arnold","Declan Rice","Jude Bellingham","Bukayo Saka",
                "Phil Foden","Harry Kane","Raheem Sterling"
            )).also { id = it }

            // ── GRUPO B: IRÁN ──
            agregarJugadores(laminas, id, "IRN", "Irán", "GRUPO B", listOf(
                "Alireza Beiranvand","Ehsan Hajsafi","Morteza Pouraliganji","Shoja Khalilzadeh",
                "Mohammad Mohebi","Saeid Ezatolahi","Ahmad Nourollahi","Ali Gholizadeh",
                "Mehdi Taremi","Sardar Azmoun","Karim Ansarifard"
            )).also { id = it }

            // ── GRUPO B: ESTADOS UNIDOS ──
            agregarJugadores(laminas, id, "USA", "Estados Unidos", "GRUPO B", listOf(
                "Matt Turner","Sergiño Dest","Walker Zimmerman","Miles Robinson",
                "Antonee Robinson","Tyler Adams","Weston McKennie","Christian Pulisic",
                "Giovanni Reyna","Timothy Weah","Josh Sargent"
            )).also { id = it }

            // ── GRUPO B: GALES ──
            agregarJugadores(laminas, id, "WAL", "Gales", "GRUPO B", listOf(
                "Wayne Hennessey","Ben Davies","Chris Mepham","Connor Roberts",
                "Joe Allen","Aaron Ramsey","Harry Wilson","Daniel James",
                "Gareth Bale","Kieffer Moore","Mark Harris"
            )).also { id = it }

            // ── GRUPO C: ARGENTINA ──
            agregarJugadores(laminas, id, "ARG", "Argentina", "GRUPO C", listOf(
                "Emiliano Martinez","Nicolas Otamendi","Cristian Romero","Nicolas Tagliafico",
                "Nahuel Molina","Rodrigo De Paul","Leandro Paredes","Alexis Mac Allister",
                "Angel Di Maria","Lautaro Martinez","Lionel Messi"
            )).also { id = it }

            // ── GRUPO C: ARABIA SAUDITA ──
            agregarJugadores(laminas, id, "KSA", "Arabia Saudita", "GRUPO C", listOf(
                "Mohammed Al-Owais","Sultan Al-Ghannam","Ali Al-Bulaihi","Abdulelah Al-Malki",
                "Hassan Tambakti","Salman Al-Faraj","Mohamed Kanno","Abdullah Otayf",
                "Firas Al-Buraikan","Salem Al-Dawsari","Hattan Bahebri"
            )).also { id = it }

            // ── GRUPO C: MÉXICO ──
            agregarJugadores(laminas, id, "MEX", "México", "GRUPO C", listOf(
                "Guillermo Ochoa","Nestor Araujo","Cesar Montes","Jorge Sanchez",
                "Jesus Gallardo","Hector Herrera","Andres Guardado","Hirving Lozano",
                "Alexis Vega","Raul Jimenez","Henry Martin"
            )).also { id = it }

            // ── GRUPO C: POLONIA ──
            agregarJugadores(laminas, id, "POL", "Polonia", "GRUPO C", listOf(
                "Wojciech Szczesny","Jan Bednarek","Kamil Glik","Bartosz Bereszynski",
                "Matty Cash","Grzegorz Krychowiak","Piotr Zielinski","Kamil Jozwiak",
                "Sebastian Szymanski","Robert Lewandowski","Krzysztof Piatek"
            )).also { id = it }

            // ── GRUPO D: FRANCIA ──
            agregarJugadores(laminas, id, "FRA", "Francia", "GRUPO D", listOf(
                "Hugo Lloris","Raphael Varane","Dayot Upamecano","Lucas Hernandez",
                "Theo Hernandez","N'Golo Kante","Aurelien Tchouameni","Antoine Griezmann",
                "Ousmane Dembele","Karim Benzema","Kylian Mbappe"
            )).also { id = it }

            // ── GRUPO D: AUSTRALIA ──
            agregarJugadores(laminas, id, "AUS", "Australia", "GRUPO D", listOf(
                "Mathew Ryan","Milos Degenek","Harry Souttar","Aziz Behich",
                "Nathaniel Atkinson","Jackson Irvine","Aaron Mooy","Martin Boyle",
                "Ajdin Hrustic","Mathew Leckie","Mitchell Duke"
            )).also { id = it }

            // ── GRUPO D: DINAMARCA ──
            agregarJugadores(laminas, id, "DEN", "Dinamarca", "GRUPO D", listOf(
                "Kasper Schmeichel","Simon Kjaer","Andreas Christensen","Joakim Maehle",
                "Daniel Wass","Thomas Delaney","Christian Eriksen","Andreas Skov Olsen",
                "Mikkel Damsgaard","Martin Braithwaite","Jonas Wind"
            )).also { id = it }

            // ── GRUPO D: TÚNEZ ──
            agregarJugadores(laminas, id, "TUN", "Túnez", "GRUPO D", listOf(
                "Aymen Dahmen","Dylan Bronn","Yassine Meriah","Montassar Talbi",
                "Ali Maaloul","Ellyes Skhiri","Aissa Laidouni","Hannibal Mejbri",
                "Naim Sliti","Youssef Msakni","Wahbi Khazri"
            )).also { id = it }

            // ── GRUPO E: ESPAÑA ──
            agregarJugadores(laminas, id, "ESP", "España", "GRUPO E", listOf(
                "Unai Simon","Cesar Azpilicueta","Aymeric Laporte","Jordi Alba",
                "Dani Carvajal","Sergio Busquets","Pedri","Gavi",
                "Ferran Torres","Alvaro Morata","Marco Asensio"
            )).also { id = it }

            // ── GRUPO E: ALEMANIA ──
            agregarJugadores(laminas, id, "GER", "Alemania", "GRUPO E", listOf(
                "Manuel Neuer","Antonio Rudiger","Niklas Sule","David Raum",
                "Joshua Kimmich","Leon Goretzka","Ilkay Gundogan","Leroy Sane",
                "Serge Gnabry","Thomas Muller","Kai Havertz"
            )).also { id = it }

            // ── GRUPO E: JAPÓN ──
            agregarJugadores(laminas, id, "JPN", "Japón", "GRUPO E", listOf(
                "Shuichi Gonda","Shogo Taniguchi","Maya Yoshida","Yuto Nagatomo",
                "Hiroki Sakai","Wataru Endo","Genki Haraguchi","Daichi Kamada",
                "Takumi Minamino","Kaoru Mitoma","Ritsu Doan"
            )).also { id = it }

            // ── GRUPO E: COSTA RICA ──
            agregarJugadores(laminas, id, "CRC", "Costa Rica", "GRUPO E", listOf(
                "Keylor Navas","Oscar Duarte","Francisco Calvo","Bryan Oviedo",
                "Keysher Fuller","Yeltsin Tejeda","Celso Borges","Joel Campbell",
                "Alvaro Zamora","Bryan Ruiz","Anthony Contreras"
            )).also { id = it }

            // ── GRUPO F: BÉLGICA ──
            agregarJugadores(laminas, id, "BEL", "Bélgica", "GRUPO F", listOf(
                "Thibaut Courtois","Toby Alderweireld","Jan Vertonghen","Axel Witsel",
                "Thomas Meunier","Youri Tielemans","Kevin De Bruyne","Dries Mertens",
                "Eden Hazard","Romelu Lukaku","Leandro Trossard"
            )).also { id = it }

            // ── GRUPO F: CANADÁ ──
            agregarJugadores(laminas, id, "CAN", "Canadá", "GRUPO F", listOf(
                "Milan Borjan","Alistair Johnston","Kamal Miller","Samuel Adekugbe",
                "Richie Laryea","Stephen Eustaquio","Mark-Anthony Kaye","Tajon Buchanan",
                "Jonathan David","Cyle Larin","Alphonso Davies"
            )).also { id = it }

            // ── GRUPO F: MARRUECOS ──
            agregarJugadores(laminas, id, "MAR", "Marruecos", "GRUPO F", listOf(
                "Yassine Bounou","Achraf Hakimi","Nayef Aguerd","Romain Saiss",
                "Noussair Mazraoui","Sofyan Amrabat","Azzedine Ounahi","Hakim Ziyech",
                "Sofiane Boufal","Youssef En-Nesyri","Abdessamad Ezzalzouli"
            )).also { id = it }

            // ── GRUPO F: CROACIA ──
            agregarJugadores(laminas, id, "CRO", "Croacia", "GRUPO F", listOf(
                "Dominik Livakovic","Dejan Lovren","Domagoj Vida","Borna Sosa",
                "Josip Juranovic","Marcelo Brozovic","Mateo Kovacic","Luka Modric",
                "Nikola Vlasic","Ivan Perisic","Andrej Kramaric"
            )).also { id = it }

            // ── GRUPO G: BRASIL ──
            agregarJugadores(laminas, id, "BRA", "Brasil", "GRUPO G", listOf(
                "Alisson","Thiago Silva","Marquinhos","Alex Sandro",
                "Danilo","Casemiro","Lucas Paqueta","Raphinha",
                "Neymar","Vinicius Junior","Richarlison"
            )).also { id = it }

            // ── GRUPO G: SERBIA ──
            agregarJugadores(laminas, id, "SRB", "Serbia", "GRUPO G", listOf(
                "Vanja Milinkovic-Savic","Strahinja Pavlovic","Nikola Milenkovic","Filip Mladenovic",
                "Nemanja Gudelj","Sergej Milinkovic-Savic","Nemanja Maksimovic","Filip Kostic",
                "Duan Vlahovic","Aleksandar Mitrovic","Andrija Zivkovic"
            )).also { id = it }

            // ── GRUPO G: SUIZA ──
            agregarJugadores(laminas, id, "SUI", "Suiza", "GRUPO G", listOf(
                "Yann Sommer","Nico Elvedi","Manuel Akanji","Ricardo Rodriguez",
                "Silvan Widmer","Granit Xhaka","Remo Freuler","Ruben Vargas",
                "Xherdan Shaqiri","Breel Embolo","Haris Seferovic"
            )).also { id = it }

            // ── GRUPO G: CAMERÚN ──
            agregarJugadores(laminas, id, "CMR", "Camerún", "GRUPO G", listOf(
                "Andre Onana","Michael Ngadeu","Nicolas Nkoulou","Collins Fai",
                "Jean-Charles Castelletto","Andre-Frank Zambo Anguissa","Martin Hongla","Christian Toko Ekambi",
                "Bryan Mbeumo","Vincent Aboubakar","Karl Toko Ekambi"
            )).also { id = it }

            // ── GRUPO H: PORTUGAL ──
            agregarJugadores(laminas, id, "POR", "Portugal", "GRUPO H", listOf(
                "Diogo Costa","Ruben Dias","Pepe","Nuno Mendes",
                "Joao Cancelo","Joao Palhinha","Bernardo Silva","Bruno Fernandes",
                "Joao Felix","Rafael Leao","Cristiano Ronaldo"
            )).also { id = it }

            // ── GRUPO H: GHANA ──
            agregarJugadores(laminas, id, "GHA", "Ghana", "GRUPO H", listOf(
                "Lawrence Ati-Zigi","Alexander Djiku","Daniel Amartey","Baba Rahman",
                "Tariq Lamptey","Thomas Partey","Mohammed Kudus","Jordan Ayew",
                "Antoine Semenyo","Iñaki Williams","Felix Afena-Gyan"
            )).also { id = it }

            // ── GRUPO H: URUGUAY ──
            agregarJugadores(laminas, id, "URU", "Uruguay", "GRUPO H", listOf(
                "Sergio Rochet","Diego Godin","Jose Gimenez","Mathias Olivera",
                "Nahitan Nandez","Federico Valverde","Rodrigo Bentancur","Facundo Pellistri",
                "Giorgian De Arrascaeta","Luis Suarez","Darwin Nunez"
            )).also { id = it }

            // ── GRUPO H: COREA DEL SUR ──
            agregarJugadores(laminas, id, "KOR", "Corea del Sur", "GRUPO H", listOf(
                "Kim Seung-gyu","Kim Min-jae","Kim Young-gwon","Kim Jin-su",
                "Kim Moon-hwan","Jung Woo-young","Hwang In-beom","Lee Jae-sung",
                "Son Heung-min","Cho Gue-sung","Hwang Hee-chan"
            )).also { id = it }

            // ── ICONOS / LEYENDAS ──
            val iconos = listOf(
                "Pelé", "Diego Maradona", "Johan Cruyff", "Ronaldo Nazario",
                "Zinedine Zidane", "Ronaldinho", "Thierry Henry", "Paolo Maldini",
                "Lothar Matthäus", "Franz Beckenbauer", "Roberto Carlos"
            )
            iconos.forEach { nombre ->
                laminas.add(Lamina(id++, "ICO-${id-1}", nombre, "Leyenda", "ICONOS"))
            }

            dao.insertarTodas(laminas)
        }

        private fun agregarJugadores(
            lista: MutableList<Lamina>,
            idInicio: Int,
            codigoPais: String,
            pais: String,
            grupo: String,
            jugadores: List<String>
        ): Int {
            var id = idInicio
            jugadores.forEach { nombre ->
                lista.add(Lamina(id, "$codigoPais-${id}", nombre, pais, grupo))
                id++
            }
            return id
        }
    }
}
