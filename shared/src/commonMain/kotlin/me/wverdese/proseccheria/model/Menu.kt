package me.wverdese.proseccheria.model

typealias Menu = List<MenuItem>

val menu: Menu = listOf(
    /* Antipasti */
    Food(id = "FA-01", type = Food.Type.Antipasto, name = "Tagliere salumi e formaggi"),
    Food(id = "FA-02", type = Food.Type.Antipasto, name = "Carpaccio di manzo"),
    Food(id = "FA-03", type = Food.Type.Antipasto, name = "Burrata"),
    Food(id = "FA-04", type = Food.Type.Antipasto, name = "Bruschetta classica"),
    Food(id = "FA-05", type = Food.Type.Antipasto, name = "Impepata di cozze"),
    /* Secondi */
    Food(id = "FS-01", type = Food.Type.Secondo, name = "Carrè di agnello al forno"),
    Food(id = "FS-01", type = Food.Type.Secondo, name = "Pistacchio con salmone"),
    /* Primi */
    Food(id = "FP-01", type = Food.Type.Primo, name = "Linguine carbonara"),
    Food(id = "FP-02", type = Food.Type.Primo, name = "Ravioli porcini"),
    Food(id = "FP-03", type = Food.Type.Primo, name = "Rigatoni alla Bartinli"),
    Food(id = "FP-04", type = Food.Type.Primo, name = "Tagliolini vongole e peperoncino"),
    Food(id = "FP-05", type = Food.Type.Primo, name = "Tagliatelle al pesto"),
    Food(id = "FP-06", type = Food.Type.Primo, name = "Tagliolini con scampi"),
    Food(id = "FP-07", type = Food.Type.Primo, name = "Risotto ai frutti di mare"),
    /* Dolci */
    Food(id = "FD-01", type = Food.Type.Dolce, name = "Tiramisù alla nocciola"),
    Food(id = "FD-02", type = Food.Type.Dolce, name = "Pannacotta"),
    Food(id = "FD-03", type = Food.Type.Dolce, name = "Gelato"),

    /* Prosecchi e spumanti */
    Wine(id = "WP-01", type = Wine.Type.Prosecco, vessel = Wine.Vessel.BOTH, name = "Valdobbiadene Ex Dry"),
    Wine(id = "WP-02", type = Wine.Type.Prosecco, vessel = Wine.Vessel.BOTH, name = "Glera sovrano Cuvee Brut"),
    Wine(id = "WP-03", type = Wine.Type.Prosecco, vessel = Wine.Vessel.BOTH, name = "Glera sovrano Cuvee Ex Dry"),
    Wine(id = "WP-04", type = Wine.Type.Prosecco, vessel = Wine.Vessel.BOTTLE, name = "Mont Blanc Cuvee Ex Dry"),
    Wine(id = "WP-05", type = Wine.Type.Prosecco, vessel = Wine.Vessel.BOTH, name = "Conte Collalto Ex Dry"),
    Wine(id = "WP-06", type = Wine.Type.Prosecco, vessel = Wine.Vessel.BOTH, name = "Conte Collalto Brut"),
    Wine(id = "WP-07", type = Wine.Type.Prosecco, vessel = Wine.Vessel.BOTH, name = "Asolo 57 Ex Dry"),
    /* Spumanti rosè */
    Wine(id = "WS-01", type = Wine.Type.Spumante, vessel = Wine.Vessel.BOTH, name = "Mont Rosè Cuvee Dry"),
    Wine(id = "WS-02", type = Wine.Type.Spumante, vessel = Wine.Vessel.BOTTLE, name = "Collalto Rosè Ex Dry"),
    /* Vini rossi */
    Wine(id = "WR-01", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTH, name = "Chianti Caligiano"),
    Wine(id = "WR-02", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTH, name = "Chianti Riserva"),
    Wine(id = "WR-03", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTTLE, name = "Dolcetto d'Alba"),
    Wine(id = "WR-04", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTTLE, name = "Vino Nobile di Montepulciano"),
    Wine(id = "WR-05", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTH, name = "Rosso di Montalcino"),
    Wine(id = "WR-06", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTH, name = "Barbera d'Alba"),
    Wine(id = "WR-07", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTH, name = "Barbera d'Asti"),
    Wine(id = "WR-08", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTH, name = "Primitivo di Manduria"),
    Wine(id = "WR-09", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTH, name = "Villa Leoni Montepulciano"),
    Wine(id = "WR-10", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTH, name = "Valpolicella Ripasso"),
    Wine(id = "WR-11", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTTLE, name = "Frapatto Sicilia"),
    Wine(id = "WR-12", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTTLE, name = "Non Cunfunditur"),
    Wine(id = "WR-13", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTH, name = "Thalia Etna 2018"),
    Wine(id = "WR-14", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTH, name = "Barolo di Perticane"),
    Wine(id = "WR-15", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTTLE, name = "Barolo di Monchiero"),
    Wine(id = "WR-16", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTTLE, name = "Brunello di Montalcino"),
    Wine(id = "WR-17", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTTLE, name = "Brunello di San Lorenzo"),
    Wine(id = "WR-18", type = Wine.Type.Rosso, vessel = Wine.Vessel.BOTTLE, name = "Chianti Classico"),
    /* Vini bianchi */
    Wine(id = "WB-01", type = Wine.Type.Bianco, vessel = Wine.Vessel.BOTH, name = "Soave Superiore Castellaro"),
    Wine(id = "WB-02", type = Wine.Type.Bianco, vessel = Wine.Vessel.BOTH, name = "Pinot Grigio Cantina Monteforte"),
    Wine(id = "WB-03", type = Wine.Type.Bianco, vessel = Wine.Vessel.BOTTLE, name = "Pinot Grigio Monteliana"),
    Wine(id = "WB-04", type = Wine.Type.Bianco, vessel = Wine.Vessel.BOTH, name = "Chardonnay"),
    Wine(id = "WB-05", type = Wine.Type.Bianco, vessel = Wine.Vessel.BOTH, name = "Verdicchio Cast.Jesi"),
    Wine(id = "WB-06", type = Wine.Type.Bianco, vessel = Wine.Vessel.BOTTLE, name = "Inzolia Sicilia"),
    Wine(id = "WB-07", type = Wine.Type.Bianco, vessel = Wine.Vessel.BOTTLE, name = "Roero Arneis"),
    Wine(id = "WB-08", type = Wine.Type.Bianco, vessel = Wine.Vessel.BOTTLE, name = "Bianco Sicilia Etna"),
    Wine(id = "WB-09", type = Wine.Type.Bianco, vessel = Wine.Vessel.BOTH, name = "Bardolino Chiaretto Rosè"),
    /* Vini dessert */
    Wine(id = "WD-01", type = Wine.Type.Dessert, vessel = Wine.Vessel.GLASS, name = "Recioto di Soave"),
    Wine(id = "WD-02", type = Wine.Type.Dessert, vessel = Wine.Vessel.GLASS, name = "Passito di Lacrima"),
    Wine(id = "WD-03", type = Wine.Type.Dessert, vessel = Wine.Vessel.GLASS, name = "Moscato Piemonte"),

    /* Spirits & Cocktails */
    Other(id = "SC-01", type = Other.Type.Spirit, name = "Aperol Spritz"),
    Other(id = "SC-02", type = Other.Type.Spirit, name = "Hendricks"),
    Other(id = "SC-03", type = Other.Type.Spirit, name = "Espresso Martini"),
    Other(id = "SC-04", type = Other.Type.Spirit, name = "HUGO"),
    Other(id = "SC-05", type = Other.Type.Spirit, name = "Glennfidditch"),
    Other(id = "SC-06", type = Other.Type.Spirit, name = "Irish coffee"),
    Other(id = "SC-07", type = Other.Type.Spirit, name = "Sambuca"),
    Other(id = "SC-08", type = Other.Type.Spirit, name = "Amaretto"),
    Other(id = "SC-09", type = Other.Type.Spirit, name = "Limoncello"),
    Other(id = "SC-10", type = Other.Type.Spirit, name = "Baileys"),
    Other(id = "SC-11", type = Other.Type.Spirit, name = "Grappa"),
    Other(id = "SC-12", type = Other.Type.Spirit, name = "De Luze XO"),
    /* Beers */
    Other(id = "B-01", type = Other.Type.Beer, name = "Poretti 0,4 lt"),
    Other(id = "B-02", type = Other.Type.Beer, name = "Ringnes 0,4 lt"),
    Other(id = "B-03", type = Other.Type.Beer, name = "Ringnes 0,6 lt"),
    Other(id = "B-04", type = Other.Type.Beer, name = "Brooklyn lager bottle"),
    /* Water & Soft Drinks */
    Other(id = "SO-01", type = Other.Type.Soft, name = "Any type"),
    Other(id = "SO-02", type = Other.Type.Soft, name = "Purezza 0,33 lt"),
    Other(id = "SO-03", type = Other.Type.Soft, name = "Purezza 0,5 lt"),
    Other(id = "SO-04", type = Other.Type.Soft, name = "Gazzosa"),
    Other(id = "SO-05", type = Other.Type.Soft, name = "Aranciata"),
    Other(id = "SO-06", type = Other.Type.Soft, name = "Alcohol-Free Beer"),
    /* Cafeteria */
    Other(id = "C-01", type = Other.Type.Cafeteria, name = "Espresso"),
    Other(id = "C-02", type = Other.Type.Cafeteria, name = "Cappuccino"),
    Other(id = "C-03", type = Other.Type.Cafeteria, name = "Caffelatte"),
    Other(id = "C-04", type = Other.Type.Cafeteria, name = "Tea"),
    Other(id = "C-05", type = Other.Type.Cafeteria, name = "Americano"),
    Other(id = "C-06", type = Other.Type.Cafeteria, name = "Mocha"),
    Other(id = "C-07", type = Other.Type.Cafeteria, name = "Hot chocolate"),
    Other(id = "C-08", type = Other.Type.Cafeteria, name = "Cortado"),
)
