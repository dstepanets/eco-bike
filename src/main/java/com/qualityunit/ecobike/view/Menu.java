package com.qualityunit.ecobike.view;

import com.qualityunit.ecobike.controller.command.AddBikeCommand;
import com.qualityunit.ecobike.controller.command.Executable;
import com.qualityunit.ecobike.controller.command.FindItemCommand;
import com.qualityunit.ecobike.controller.command.MenuCommand;
import com.qualityunit.ecobike.controller.command.ShowCatalogCommand;
import com.qualityunit.ecobike.controller.command.StopProgramCommand;
import com.qualityunit.ecobike.controller.command.WriteToFileCommand;
import com.qualityunit.ecobike.model.AbstractBike;
import com.qualityunit.ecobike.model.BikeType;
import com.qualityunit.ecobike.model.CatalogPage;
import com.qualityunit.ecobike.model.ElectricBike;
import com.qualityunit.ecobike.model.FoldingBike;
import com.qualityunit.ecobike.model.Storage;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.System.err;
import static java.lang.System.out;

public class Menu {
	public static final String FILE_PATH_PROMPT = "Please enter the input file path:";
	public static final String OPEN_ERROR_MSG = "Can't open a file at this path: '%s'";
	public static final String SEPARATOR = "- - - - - - - - - - - - - - - - - - - - - - -";

	private static Menu menuInstance;
	private final Map<Integer, MenuCommand> numToCommand;

	private Menu() {
		numToCommand = new LinkedHashMap<>();
		numToCommand.put(1, new ShowCatalogCommand("Show the entire EcoBike catalog"));
		numToCommand.put(2, new AddBikeCommand("Add a new folding bike", BikeType.FOLDING_BIKE));
		numToCommand.put(3, new AddBikeCommand("Add a new speedelec", BikeType.SPEEDELEC));
		numToCommand.put(4, new AddBikeCommand("Add a new e-bike", BikeType.EBIKE));
		numToCommand.put(5, new FindItemCommand("Find the first item of a particular brand"));
		numToCommand.put(6, new WriteToFileCommand("Write to file"));
		numToCommand.put(7, new StopProgramCommand("Stop the program"));
	}

	public static synchronized Menu getInstance() {
		if (menuInstance == null) {
			menuInstance = new Menu();
		}
		return menuInstance;
	}

	private void displayMenu() {
		out.println(SEPARATOR);
		out.println("Please make your choice:");
		numToCommand.forEach((k, c) -> out.println(k + " - " + c.getDescription()));
		out.println(SEPARATOR);
	}

	public Executable getCommandFromUser() {
		MenuCommand command;
		do {
			displayMenu();
			int choice = UserInput.getInt("Enter a number:");
			command = numToCommand.get(choice);
			if (command == null) {
				err.println("Wrong option");
			} else {
				out.println("--> " + command.getDescription());
			}
		} while (command == null);
		return command;
	}

	public String displayCatalogPage(CatalogPage page) {
		String pageStats = String.format("---[Page: %d/%d | Items: %d-%d/%d]---",
				page.getCurrentPage(), page.getTotalPages(),
				page.getFirstItemNum(), page.getLastItemNum(),
				page.getTotalItems());
		out.println("\n" + pageStats);
		page.getItems().forEach(i -> out.println(i.toDisplayFormatString()));
		out.println(pageStats);
		String prompt =
				"\n('N' - next page | 'P' - previous page | 'M' - back to menu | <number> - go to this page)\n" +
					"Your command: ";
		return UserInput.getLine(prompt);
	}

	public AbstractBike constructBikeFromUserInput(BikeType bikeType, boolean isSearchQuery) {
		out.println("NEW " + bikeType.toString());
		String brandName = UserInput.getLine("Enter its brand name:");
		int weight = UserInput.getNonNegativeInt("Enter the weight of the bike (in grams):");
		boolean hasLights = UserInput.getBoolean("Does it have front and back lights?");
		String color = UserInput.getLine("Enter the color:");
		int price = UserInput.getNonNegativeInt("Enter the price:");

		AbstractBike bike = null;
		switch (bikeType) {
			case FOLDING_BIKE:
				bike = FoldingBike.getBuilder()
						.withBikeType(bikeType)
						.withBrand(brandName)
						.withWeight(weight)
						.withHasLights(hasLights)
						.withColor(color)
						.withPrice(price)
						.withWheelSize(UserInput.getNonNegativeInt("Enter the size of the wheels (in inches):"))
						.withGearsNum(UserInput.getNonNegativeInt("Enter the number of gears:"))
						.build();
				break;
			case EBIKE:
			case SPEEDELEC:
				bike = ElectricBike.getBuilder()
						.withBikeType(bikeType)
						.withBrand(brandName)
						.withWeight(weight)
						.withHasLights(hasLights)
						.withColor(color)
						.withPrice(price)
						.withMaxSpeed(UserInput.getNonNegativeInt("Enter the maximum speed (in km/h):"))
						.withBatteryCapacity(UserInput.getNonNegativeInt("Enter the battery capacity (in mAh):"))
						.build();
				break;
		}
		return bike;
	}
}
