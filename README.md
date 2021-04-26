# Library
Biblioteksprojektet för Programmering i Java II

---

### Applikationsarkitektur

I och med att definitionen av MVC kan skilja mellan olika miljöer/språk har vi definerat arkitekturen på följande sätt:

- **Modell**: Den synnerligen mest centrala komponenten som ansvarar för att hantera applikationens data, logik och regler.
- **Vy**: All visuell representation av information.
- **Kontroller**: Limmet mellan vyn och modellen. Tar emot inmatning och för detta vidare till antingen en vy eller modell.

Ovanstående är baserat på [följande](https://en.wikipedia.org/wiki/Model–view–controller).
