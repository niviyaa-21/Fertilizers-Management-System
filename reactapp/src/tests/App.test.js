// __tests__/App.test.js
import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import axios from "axios";
import App from "../App";
import FertilizerForm from "../components/FertilizerForm";
import FertilizerList from "../components/FertilizerList";
import Header from "../components/Header";

jest.mock("axios");

const mockFertilizers = [
  { id: 1, fertilizerName: "Super Grow NPK", manufacturer: "AgroTech Industries", type: "Chemical", quantity: 50, price: 1250 },
  { id: 2, fertilizerName: "Organic Compost", manufacturer: "Green Fields", type: "Organic", quantity: 100, price: 800 },
  { id: 3, fertilizerName: "Liquid Boost", manufacturer: "FarmMax Corp", type: "Liquid", quantity: 25, price: 2000 },
];

// ----------------- HEADER TESTS -----------------
describe("Header Component", () => {
  test("renders title", () => {
    render(<Header />);
    expect(screen.getByText("Fertilizer Management System")).toBeInTheDocument();
  });

  test("renders tagline", () => {
    render(<Header />);
    expect(screen.getByText("Add • Filter • Sort • Manage fertilizers effortlessly")).toBeInTheDocument();
  });
});

// ----------------- FERTILIZER FORM TESTS -----------------
describe("FertilizerForm Component", () => {
  test("renders add fertilizer form", () => {
    render(<FertilizerForm onAdd={jest.fn()} setError={jest.fn()} />);
    expect(screen.getByText("Add Fertilizer")).toBeInTheDocument();
  });

  test("submits new fertilizer successfully", async () => {
    axios.post.mockResolvedValue({ data: { id: 4 } });
    const onAddMock = jest.fn();

    render(<FertilizerForm onAdd={onAddMock} setError={jest.fn()} />);

    fireEvent.change(screen.getByPlaceholderText("Fertilizer name"), { target: { value: "Premium NPK", name: "fertilizerName" } });
    fireEvent.change(screen.getByPlaceholderText("Manufacturer"), { target: { value: "BestGrow Inc", name: "manufacturer" } });
    fireEvent.change(screen.getByPlaceholderText("Quantity"), { target: { value: "75", name: "quantity" } });
    fireEvent.change(screen.getByPlaceholderText("Price"), { target: { value: "1500", name: "price" } });

    fireEvent.click(screen.getByText("Add Fertilizer"));

    await waitFor(() => {
      expect(axios.post).toHaveBeenCalled();
      expect(onAddMock).toHaveBeenCalled();
    });
  });

  test("handles API error on add", async () => {
    axios.post.mockRejectedValue(new Error("Network Error"));
    const setErrorMock = jest.fn();
    render(<FertilizerForm onAdd={jest.fn()} setError={setErrorMock} />);

    fireEvent.change(screen.getByPlaceholderText("Fertilizer name"), { target: { value: "Premium NPK", name: "fertilizerName" } });
    fireEvent.click(screen.getByText("Add Fertilizer"));

    await waitFor(() => expect(setErrorMock).toHaveBeenCalled());
  });

  test("loads fertilizer data for editing", async () => {
    axios.get.mockResolvedValue({ data: mockFertilizers[0] });
    render(
      <MemoryRouter initialEntries={["/edit/1"]}>
        <Routes>
          <Route path="/edit/:id" element={<FertilizerForm onAdd={jest.fn()} setError={jest.fn()} editId="1" />} />
        </Routes>
      </MemoryRouter>
    );

    await waitFor(() => screen.getByDisplayValue("Super Grow NPK"));
    expect(screen.getByDisplayValue("Super Grow NPK")).toBeInTheDocument();
    expect(screen.getByText("Update Fertilizer")).toBeInTheDocument();
  });

  test("updates fertilizer successfully", async () => {
    axios.get.mockResolvedValue({ data: mockFertilizers[0] });
    axios.put.mockResolvedValue({ data: { ...mockFertilizers[0], price: 1300 } });
    const onAddMock = jest.fn();

    render(
      <MemoryRouter initialEntries={["/edit/1"]}>
        <Routes>
          <Route path="/edit/:id" element={<FertilizerForm onAdd={onAddMock} setError={jest.fn()} editId="1" />} />
        </Routes>
      </MemoryRouter>
    );

    await waitFor(() => screen.getByDisplayValue("Super Grow NPK"));

    fireEvent.change(screen.getByDisplayValue("1250"), { target: { value: "1300", name: "price" } });
    fireEvent.click(screen.getByText("Update Fertilizer"));

    await waitFor(() => {
      expect(axios.put).toHaveBeenCalled();
      expect(onAddMock).toHaveBeenCalled();
    });
  });

  test("handles API error on update", async () => {
    axios.get.mockResolvedValue({ data: mockFertilizers[0] });
    axios.put.mockRejectedValue(new Error("Update failed"));
    const setErrorMock = jest.fn();

    render(
      <MemoryRouter initialEntries={["/edit/1"]}>
        <Routes>
          <Route path="/edit/:id" element={<FertilizerForm onAdd={jest.fn()} setError={setErrorMock} editId="1" />} />
        </Routes>
      </MemoryRouter>
    );

    await waitFor(() => screen.getByDisplayValue("Super Grow NPK"));

    fireEvent.change(screen.getByDisplayValue("1250"), { target: { value: "1300", name: "price" } });
    fireEvent.click(screen.getByText("Update Fertilizer"));

    await waitFor(() => expect(setErrorMock).toHaveBeenCalled());
  });

  test("form validation: empty fields", async () => {
    const setErrorMock = jest.fn();
    render(<FertilizerForm onAdd={jest.fn()} setError={setErrorMock} />);

    fireEvent.click(screen.getByText("Add Fertilizer"));
    await waitFor(() => expect(setErrorMock).toHaveBeenCalled());
  });

  test("displays success message on add/update", async () => {
    axios.post.mockResolvedValue({});
    render(<FertilizerForm onAdd={jest.fn()} setError={jest.fn()} />);
    fireEvent.change(screen.getByPlaceholderText("Fertilizer name"), { target: { value: "Bio Organic", name: "fertilizerName" } });
    fireEvent.change(screen.getByPlaceholderText("Manufacturer"), { target: { value: "EcoFarm Ltd", name: "manufacturer" } });
    fireEvent.change(screen.getByPlaceholderText("Quantity"), { target: { value: "60", name: "quantity" } });
    fireEvent.change(screen.getByPlaceholderText("Price"), { target: { value: "900", name: "price" } });

    fireEvent.click(screen.getByText("Add Fertilizer"));
    await waitFor(() => screen.getByText("Fertilizer added successfully"));
  });

  // ----------------- ADDITIONAL TESTS -----------------
describe("Additional Fertilizer Management Tests", () => {
  test("updates fertilizer name correctly", async () => {
    axios.get.mockResolvedValue({ data: mockFertilizers[0] });
    axios.put.mockResolvedValue({ data: { ...mockFertilizers[0], fertilizerName: "Super Grow NPK Premium" } });
    const onAddMock = jest.fn();

    render(
      <MemoryRouter initialEntries={["/edit/1"]}>
        <Routes>
          <Route path="/edit/:id" element={<FertilizerForm onAdd={onAddMock} setError={jest.fn()} editId="1" />} />
        </Routes>
      </MemoryRouter>
    );

    await waitFor(() => screen.getByDisplayValue("Super Grow NPK"));
    fireEvent.change(screen.getByDisplayValue("Super Grow NPK"), { target: { value: "Super Grow NPK Premium", name: "fertilizerName" } });
    fireEvent.click(screen.getByText("Update Fertilizer"));

    await waitFor(() => expect(onAddMock).toHaveBeenCalled());
  });

  test("renders success message on update", async () => {
    axios.get.mockResolvedValue({ data: mockFertilizers[0] });
    axios.put.mockResolvedValue({ data: mockFertilizers[0] });

    render(
      <MemoryRouter initialEntries={["/edit/1"]}>
        <Routes>
          <Route path="/edit/:id" element={<FertilizerForm onAdd={jest.fn()} setError={jest.fn()} editId="1" />} />
        </Routes>
      </MemoryRouter>
    );

    await waitFor(() => screen.getByDisplayValue("Super Grow NPK"));
    fireEvent.click(screen.getByText("Update Fertilizer"));
    await waitFor(() => screen.getByText("Fertilizer updated successfully"));
  });

  test("handles empty manufacturer field validation", async () => {
    const setErrorMock = jest.fn();
    render(<FertilizerForm onAdd={jest.fn()} setError={setErrorMock} />);
    fireEvent.change(screen.getByPlaceholderText("Fertilizer name"), { target: { value: "Test Fertilizer", name: "fertilizerName" } });
    fireEvent.click(screen.getByText("Add Fertilizer"));
    await waitFor(() => expect(setErrorMock).toHaveBeenCalledWith("Manufacturer is required"));
  });

});

});