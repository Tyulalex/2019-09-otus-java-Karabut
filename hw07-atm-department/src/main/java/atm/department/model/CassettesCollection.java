package atm.department.model;

import atm.department.app.Calculable;
import atm.department.app.Nominal;
import atm.department.configuration.CassetteConfiguration;
import atm.department.model.state.CassetteState;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CassettesCollection implements Iterable<Cassette>, Calculable {

    private List<Cassette> cassettes;

    public CassettesCollection(List<CassetteConfiguration> cassetteConfigurations) {
        this.cassettes = initCassettes(cassetteConfigurations);
    }

    public List<CassetteState> getCassettesListState() {
        List<CassetteState> cassetteStates = new ArrayList<>();
        this.cassettes.stream().forEach(cassette -> cassetteStates.add(cassette.getState()));
        return cassetteStates;
    }

    @Override
    public String toString() {
        return this.cassettes.toString();
    }

    public Cassette getCassetteByNominal(Nominal nominal) {
        return this.cassettes.stream()
                .filter(cassette -> cassette.getNominal().equals(nominal))
                .findFirst().orElse(null);
    }

    public Nominal getLowestNominal() {
        this.cassettes.sort(Collections.reverseOrder());
        return this.cassettes.get(this.cassettes.size() - 1).getNominal();
    }

    @Override
    public Iterator<Cassette> iterator() {
        return this.cassettes.iterator();
    }

    @Override
    public void forEach(Consumer<? super Cassette> action) {
        this.cassettes.forEach(action);
    }

    @Override
    public Spliterator<Cassette> spliterator() {
        return this.cassettes.spliterator();
    }

    public Stream<Cassette> stream() {
        return this.cassettes.stream();
    }

    @Override
    public int getAmountOfMoney() {
        return this.cassettes.stream().mapToInt(Cassette::getAmountOfMoney).sum();
    }

    private List<Cassette> initCassettes(List<CassetteConfiguration> cassetteConfiguration) {
        List<Cassette> cassettes = new ArrayList<>();
        for (CassetteConfiguration configuration : cassetteConfiguration) {
            cassettes.add(new Cassette(configuration));
        }
        cassettes.sort(Collections.reverseOrder());
        return cassettes;
    }
}
