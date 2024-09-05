package de.unisaarland.cs.se.sopra.crossroad;

import de.unisaarland.cs.se.sopra.config.ParamMap;
import java.util.List;
import org.json.JSONException;

public class CrossroadFactoryImpl implements CrossroadFactory<Crossroad> {

    @Override
    public Crossroad createCrossroad(final int id, final String type,
                                     final ParamMap crossroadParams,
                                     final List<Consequence> consequences) {
        return switch (type) {

            case "barricaded" -> {
                try {
                    final int locId = crossroadParams.getInt("locationId");
                    yield new BarricadedCrossroad(id, CrossroadType.BARRICADED, consequences,
                            locId);
                } catch (final JSONException e) {
                    yield new BarricadedNoLocCrossroad(id, CrossroadType.BARRICADED, consequences);
                }
            }

            case "moved" -> {
                try {
                    final int locId = crossroadParams.getInt("locationId");
                    yield new MovedCrossroad(id, CrossroadType.MOVED, consequences, locId);
                } catch (final JSONException e) {
                    yield new MovedNoLocCrossroad(id, CrossroadType.MOVED, consequences);
                }
            }

            case "searched" -> {
                try {
                    final int locId = crossroadParams.getInt("locationId");
                    yield new SearchedCrossroad(id, CrossroadType.SEARCHED, consequences, locId);
                } catch (final JSONException e) {
                    yield new SearchedNoLocCrossroad(id, CrossroadType.SEARCHED, consequences);
                }
            }

            case "wasteChanged" -> {
                final int numTrash = crossroadParams.getInt("amount");
                yield new WasteChangedCrossroad(id, CrossroadType.WASTECHANGED, consequences,
                        numTrash);
            }

            case "equip" -> {
                yield new EquipCrossroad(id, CrossroadType.EQUIP, consequences);
            }

            default -> {
                throw new IllegalArgumentException(
                        "Unknown crossroad card: %s".formatted(type));
            }
        };
    }
}
